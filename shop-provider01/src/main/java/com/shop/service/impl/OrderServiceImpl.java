package com.shop.service.impl;

import com.shop.bean.*;
import com.shop.dao.OrderDAO;
import com.shop.dao.OrderDetailDAO;
import com.shop.enums.OrderState;
import com.shop.exception.BasicException;
import com.shop.service.AddressService;
import com.shop.service.CartService;
import com.shop.service.CommodityClientService;
import com.shop.service.OrderService;
import com.shop.util.AliPayUtil;
import com.shop.util.CommonUtil;
import com.shop.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final CommodityClientService commodityService;
    private final CartService cartService;
    private final AddressService addressService;
    private final AliPayUtil aliPayUtil;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO, OrderDetailDAO orderDetailDAO, CommodityClientService commodityService, CartService cartService, AddressService addressService, AliPayUtil aliPayUtil) {
        this.orderDAO = orderDAO;
        this.orderDetailDAO = orderDetailDAO;
        this.cartService = cartService;
        this.commodityService = commodityService;
        this.addressService = addressService;
        this.aliPayUtil = aliPayUtil;
    }

    @Override
    public ResponseBody<List<Order>> getOrderList(Integer userId, Integer pageIndex, Integer pageSize) {
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andUserIdEqualTo(userId).andIsDelEqualTo(0);
        long count = orderDAO.countByExample(orderExample);
        orderExample.setOffset((long) (pageIndex - 1) * pageSize);
        orderExample.setLimit(pageSize);
        List<Order> orders = orderDAO.selectByExample(orderExample);
        Map<String, Object> map = new HashMap<>();
        map.put("total", count);
        map.put("items", orders);
        return ResponseBody.success(map);
    }

    @Override
    @Transactional
    public ResponseBody<Long> addOrder(Integer userId, Integer addressId, List<Cart> cartList) {
        Long orderId = OrderUtil.getOrderNumber();
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(userId);
        Address address = addressService.getAddressById(addressId);
        order.setAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailedAddress());
        order.setReceiverName(address.getReceiverName());
        order.setTel(address.getTel());
        List<Integer> commodityIdList = CommonUtil.getIntListFromList(cartList, "commodityId");
        ResponseBody<List<Commodity>> res = commodityService.getCommoditiesByIdList(commodityIdList);
        if (res.getCode() != 0)
            throw new BasicException(res.getMsg());
        List<Commodity> commodityList = res.getData();
        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderDetail> orderDetailList = new ArrayList<>(cartList.size());
        for (Cart cart : cartList) {
            for (Commodity commodity : commodityList) {
                if (cart.getCommodityId() == commodity.getId()) {
                    if (commodity.getStock() < cart.getQuantity())
                        throw new BasicException("商品“" + commodity.getName() + "”库存不足");
                    // 更新库存
                    commodity.setStock(commodity.getStock() - cart.getQuantity());
//                    commodity.setSales(commodity.getSales() + cart.getQuantity());
                    OrderDetail info = new OrderDetail();
                    info.setCommodityId(commodity.getId());
                    info.setCommodityName(commodity.getName());
                    info.setPrice(commodity.getPrice());
                    info.setQuantity(cart.getQuantity());
                    info.setOrderId(orderId);
                    orderDetailList.add(info);
                    totalPrice = totalPrice.add(commodity.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
                    break;
                }
            }
        }
        order.setTotalPrice(totalPrice);
        // 创建订单
        orderDAO.insert(order);
        // 添加订单明细到数据库
        orderDetailDAO.insertBatch(orderDetailList);
        // 删除购物车中相应商品
        cartService.multiDeleteCart(CommonUtil.getIntListFromList(cartList, "id"));
        // 更新数据库商品库存
        ResponseBody<Integer> commUpdateRes = commodityService.updateList(commodityList);
        if (commUpdateRes.getCode() != 0)
            throw new BasicException(commUpdateRes.getMsg());
        String param = aliPayUtil.pagePay(orderId.toString(), totalPrice.toString(), "订单支付");
//        } else if (!StringUtils.isEmpty(commodity_id)) {
//            ResponseBody<Commodity> commodityRes = commodityService.getCommodityById(commodity_id);
//            if (commodityRes.getCode() != 0)
//                throw new BasicException(commodityRes.getMsg());
//            Commodity commodity = commodityRes.getData();
//            if (commodity.getStock() < number)
//                throw new BasicException("商品“" + commodity.getName() + "”库存不足");
//            order.setTotalPrice(commodity.getPrice().multiply(new BigDecimal(number)));
//            List<Commodity> commodityList = new ArrayList<>(1);
//            commodityList.add(commodity);
//            if (commodityService.updateList(commodityList).getCode() != 0)
//                throw new BasicException("库存更新失败");
//            orderDAO.insert(order);
//            OrderDetail info = new OrderDetail();
//            info.setOrderId(orderId);
//            info.setCommodityId(commodity_id);
//            info.setCommodityName(commodity.getName());
//            info.setPrice(commodity.getPrice());
//            info.setQuantity(number);
//            orderDetailDAO.insert(info);
//        }
        return ResponseBody.success(param);
    }

    @Override
    public ResponseBody<Integer> deleteOrder(Order order) {
        order.setIsDel(1);
        return ResponseBody.success(orderDAO.updateByPrimaryKeySelective(order));
    }

    @Override
    public ResponseBody<List<OrderDetail>> getOrderDetail(Long orderId) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        return ResponseBody.success(orderDetailDAO.selectByExample(orderDetailExample));
    }

    @Override
    public ResponseBody<Integer> updateOrder(Order order) {
        Order orderOriginal = orderDAO.selectByPrimaryKey(order.getId());
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        List<OrderDetail> detailSet = orderOriginal.getDetailSet();
        List<Integer> commodityIdList = CommonUtil.getIntListFromList(detailSet, "commodityId");

//        支付 0 -> 1
        if (OrderState.NOPAY.getCode() == orderOriginal.getState() && OrderState.NOSEND.getCode() == order.getState()) {
            order.setPayTime(now);
        }
//        确认收货2 -> 3
        else if (OrderState.NORECEIVE.isEquals(orderOriginal.getState()) && OrderState.COMPLETED.isEquals(orderOriginal.getState())) {
            ResponseBody<List<Commodity>> commoditiesByIdList = commodityService.getCommoditiesByIdList(commodityIdList);
            if (commoditiesByIdList.getCode() != 0)
                throw new BasicException(commoditiesByIdList.getMsg());
            List<Commodity> commodities = commoditiesByIdList.getData();
            for (Commodity comm : commodities) {
                for (OrderDetail detail : detailSet) {
                    if (comm.getId() == detail.getCommodityId()) {
                        comm.setSales(comm.getSales() + detail.getQuantity());
                        break;
                    }

                }
            }
            commodityService.updateList(commodities);
            order.setCompleteTime(now);
        }
//        取消订单? -> -1
        else if (OrderState.COMPLETED.getCode() != orderOriginal.getState() && OrderState.CLOSED.getCode() == order.getState()) {
            ResponseBody<List<Commodity>> commoditiesByIdList = commodityService.getCommoditiesByIdList(commodityIdList);
            if (commoditiesByIdList.getCode() != 0)
                throw new BasicException(commoditiesByIdList.getMsg());
            List<Commodity> commodities = commoditiesByIdList.getData();
            for (Commodity comm : commodities) {
                for (OrderDetail detail : detailSet) {
                    if (comm.getId() == detail.getCommodityId()) {
                        comm.setStock(comm.getStock() + detail.getQuantity());
                        break;
                    }
                }
            }
            commodityService.updateList(commodities);
        }
        return ResponseBody.success(orderDAO.updateByPrimaryKeySelective(order));
    }

    @Override
    public ResponseBody<Order> getOrderById(Long orderId) {
        return ResponseBody.success(orderDAO.selectByPrimaryKey(orderId));
    }

    @Override
    public ResponseBody updateSend(Order order) {
        return ResponseBody.success(orderDAO.updateByPrimaryKeySelective(order));
    }

    @Override
    public ResponseBody spendRank(Integer pageIndex, Integer pageSize) {
        return ResponseBody.success(orderDAO.spendRank((long) (pageIndex - 1) * pageSize, pageSize));
    }

    @Override
    public ResponseBody orderQuantityRank(Integer pageIndex, Integer pageSize) {
        return ResponseBody.success(orderDAO.orderQuantityRank((long) (pageIndex - 1) * pageSize, pageSize));
    }


    @Override
    public ResponseBody getOrderAll(Integer pageIndex, Integer pageSize, Integer status) {
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.createCriteria();
        if (!StringUtils.isEmpty(status))
            criteria.andStateEqualTo(status);
        orderExample.setOffset((long) (pageIndex - 1) * pageSize);
        orderExample.setLimit(pageSize);
        long total = orderDAO.countByExample(orderExample);
        List<Order> orders = orderDAO.selectByExample(orderExample);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("items", orders);
        return ResponseBody.success(map);
    }
}
