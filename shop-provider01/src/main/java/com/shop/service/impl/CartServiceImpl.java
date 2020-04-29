package com.shop.service.impl;

import com.shop.bean.Cart;
import com.shop.bean.CartExample;
import com.shop.bean.Commodity;
import com.shop.bean.ResponseBody;
import com.shop.dao.CartDAO;
import com.shop.exception.BasicException;
import com.shop.service.CartService;
import com.shop.service.CommodityClientService;
import com.shop.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private final CartDAO dao;
    private final CommodityClientService commodityClient;

    @Autowired
    public CartServiceImpl(CartDAO dao, CommodityClientService commodityClient) {
        this.dao = dao;
        this.commodityClient = commodityClient;
    }


    @Override
    public ResponseBody<List<Cart>> getCartList(Integer userId) {
        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Cart> carts = dao.selectByExample(cartExample);
        if (carts == null || carts.isEmpty())
            return new ResponseBody<>();
        List<Integer> commodityIdList = CommonUtil.getIntListFromList(carts, "commodityId");
        ResponseBody<List<Commodity>> commodityResponse = commodityClient.getCommoditiesByIdList(commodityIdList);
        if (commodityResponse.getCode() != 0)
            throw new BasicException(commodityResponse.getMsg() + ", 购物车服务暂时不能提供服务");
        List<Commodity> commodities = commodityResponse.getData();
        if (commodities != null)
            for (int i = 0; i < carts.size(); i++) {
                for (int j = 0; j < commodities.size(); j++) {
                    if (carts.get(i).getCommodityId() == commodities.get(j).getId()) {
                        carts.get(i).setCommodity(commodities.get(j));
                        break;
                    }
                }
            }
        return ResponseBody.success(carts);
    }

    @Override
    public ResponseBody<Long> countCart(Integer userId) {
        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return ResponseBody.success(dao.countByExample(cartExample));
    }

    @Override
    public ResponseBody<Integer> addCart(Integer userId, Cart cart) {
        cart.setUserId(userId);
        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId).andCommodityIdEqualTo(cart.getCommodityId());
        List<Cart> carts = dao.selectByExample(cartExample);
        // 购物车已经有该商品
        if (!carts.isEmpty()) {
            cart.setId(carts.get(0).getId());
            cart.setQuantity(carts.get(0).getQuantity() + cart.getQuantity());
            return updateCart(cart);
        }
        return ResponseBody.success(dao.insert(cart));
    }

    @Override
    public ResponseBody<Integer> updateCart(Cart cart) {
        return new ResponseBody<>(dao.updateByPrimaryKey(cart));
    }

    @Override
    public ResponseBody<Integer> deleteCart(Cart cart) {
        return ResponseBody.success(dao.deleteByPrimaryKey(cart.getId()));
    }

    @Override
    public ResponseBody<Integer> multiDeleteCart(List<Integer> cartIdList) {
        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andIdIn(cartIdList);
        return ResponseBody.success(dao.deleteByExample(cartExample));
    }

    @Override
    public ResponseBody<Integer> deleteAllCart(Integer userId) {
        CartExample cartExample = new CartExample();
        CartExample.Criteria criteria = cartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return ResponseBody.success(dao.deleteByExample(cartExample));
    }

    @Override
    public ResponseBody<BigDecimal> getTotalPrice(List<Cart> cartList) {
        List<Integer> commodityIdList = CommonUtil.getIntListFromList(cartList, "commodityId");
        ResponseBody<List<Commodity>> commoditiesRes = commodityClient.getCommoditiesByIdList(commodityIdList);
        if (commoditiesRes.getCode() != 0)
            throw new BasicException(commoditiesRes.getMsg());
        List<Commodity> commodityList = commoditiesRes.getData();
        BigDecimal totalPrice = new BigDecimal(0);
        for (int i = 0; i < cartList.size(); i++) {
            for (int j = 0; j < commodityList.size(); j++) {
                if (cartList.get(i).getCommodityId() == commodityList.get(j).getId()) {
                    totalPrice = totalPrice.add(commodityList.get(j).getPrice().multiply(new BigDecimal(cartList.get(i).getQuantity())));
                    break;
                }
            }
        }
        return ResponseBody.success(totalPrice);
    }

    @Override
    public ResponseBody<Map<String, Object>> getCartByIdList(List<Integer> idList) {
        CartExample example = new CartExample();
        CartExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(idList);
        List<Cart> cartList = dao.selectByExample(example);
        if (cartList.isEmpty())
            throw new BasicException("请到购物车选择商品哦");
        ResponseBody<List<Commodity>> commodityResponse = commodityClient.getCommoditiesByIdList(CommonUtil.getIntListFromList(cartList, "commodityId"));
        if (commodityResponse.getCode() != 0)
            throw new BasicException(commodityResponse.getMsg());
        List<Commodity> commodities = commodityResponse.getData();
        Map<String, Object> map = new HashMap<>(2);
        map.put("cart", cartList);
        map.put("commodity", commodities);
        return ResponseBody.success(map);
    }
}
