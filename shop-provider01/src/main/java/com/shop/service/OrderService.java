package com.shop.service;

import com.shop.bean.*;
import java.util.List;

public interface OrderService {
    ResponseBody<List<Order>> getOrderList(Integer userId, Integer pageIndex, Integer pageSize);
    ResponseBody<Long> addOrder(Integer userId, Integer addressId, List<Cart> cartList);
    ResponseBody<Integer> deleteOrder(Order order);
    ResponseBody<List<OrderDetail>> getOrderDetail(Long orderId);
    ResponseBody<Integer> updateOrder(Order order);
    ResponseBody spendRank(Integer pageIndex, Integer pageSize);
    ResponseBody orderQuantityRank(Integer pageIndex, Integer pageSize);
    ResponseBody getOrderAll(Integer pageIndex, Integer pageSize, Integer status);
    ResponseBody<Order> getOrderById(Long orderId);
    ResponseBody updateSend(Order order);
}
