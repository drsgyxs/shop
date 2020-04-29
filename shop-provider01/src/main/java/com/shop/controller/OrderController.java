package com.shop.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shop.bean.*;
import com.shop.bean.ResponseBody;
import com.shop.enums.OrderState;
import com.shop.exception.BasicException;
import com.shop.service.OrderService;
import com.shop.util.AliPayUtil;
import com.shop.validate.Delete;
import com.shop.validate.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class OrderController {
    private final OrderService service;
    @Resource
    private AliPayUtil aliPayUtil;
    @Value("${aliPay.aliPayPublicKey}")
    private String ALI_PAY_PUBLIC_KEY;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/order")
    public ResponseBody<List<Order>> getOrderList(Integer userId,
                                                  @NotNull(message = "页码不能为空") Integer pageIndex,
                                                  @RequestParam(required = false, defaultValue = "8") Integer pageSize) {
        return service.getOrderList(userId, pageIndex, pageSize);
    }

    @PostMapping("/order/{addressId}")
    public ResponseBody<Long> addOrder(Integer userId, @PathVariable("addressId") Integer addressId, @RequestBody List<Cart> cartList) {
        return service.addOrder(userId, addressId, cartList);
    }

    @DeleteMapping("/order")
    public ResponseBody<Integer> deleteOrder(@Validated(Delete.class) @RequestParam Order order) {
        return service.deleteOrder(order);
    }

    @GetMapping("/order/detail/{orderId}")
    public ResponseBody<List<OrderDetail>> getOrderDetail(@PathVariable("orderId") @NotNull(message = "订单号不能为空") Long orderId) {
        return service.getOrderDetail(orderId);
    }

    @GetMapping("/admin/order/{orderId}")
    public ResponseBody<Order> getOrderById(@NotNull(message = "订单号不能为空") @PathVariable("orderId") Long orderId) {
        return service.getOrderById(orderId);
    }

    @PutMapping("/order")
    public ResponseBody<Integer> updateOrder(@Validated({Update.class}) @RequestBody Order order) {
        return service.updateOrder(order);
    }

    @GetMapping("/order/pay/{orderId}")
    public ResponseBody<String> aliPay(@PathVariable("orderId") Long orderId) {
        Order order = service.getOrderById(orderId).getData();
        if (order == null)
            throw new BasicException("订单不存在");
        String param = aliPayUtil.pagePay(orderId.toString(), order.getTotalPrice().toString(), "订单支付");
        return ResponseBody.success(param);
    }

    @PostMapping("/order/pay")
    public ResponseBody<Integer> payOrder(@RequestBody Map<String, String> paramsMap) {
        ResponseBody res = null;
        try {
            boolean signVerified = AlipaySignature.rsaCertCheckV1(paramsMap, ALI_PAY_PUBLIC_KEY, "UTF-8", "RSA2");
            if (signVerified) {
                Order order = new Order();
                order.setId(Long.valueOf(paramsMap.get("out_trade_no")));
                order.setState(OrderState.NOSEND.getCode());
                res = service.updateOrder(order);
            } else
                throw new BasicException("非法请求");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return res;
    }

    @GetMapping("/spendrank")
    public ResponseBody spendRank(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return service.spendRank(pageIndex, pageSize);
    }

    @GetMapping("/orderquantityrank")
    public ResponseBody orderQuantityRank(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return service.orderQuantityRank(pageIndex, pageSize);
    }

    @GetMapping("/admin/order")
    public ResponseBody getOrderAll(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) Integer status) {
        return service.getOrderAll(pageIndex, pageSize, status);
    }

    @PutMapping("/admin/order")
    public ResponseBody updateSend(@RequestBody Order order) {
        return service.updateSend(order);
    }

}
