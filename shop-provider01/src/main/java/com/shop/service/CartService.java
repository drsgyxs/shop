package com.shop.service;

import com.shop.bean.Cart;
import com.shop.bean.ResponseBody;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CartService {
    ResponseBody<List<Cart>> getCartList(Integer userId);
    ResponseBody<Long> countCart(Integer userId);
    ResponseBody<Integer> addCart(Integer userId,Cart cart);
    ResponseBody<Integer> updateCart(Cart cart);
    ResponseBody<Integer> deleteCart(Cart cart);
    ResponseBody<Integer> multiDeleteCart(List<Integer> cartIdList);
    ResponseBody<Integer> deleteAllCart(Integer userId);
    ResponseBody<BigDecimal> getTotalPrice(List<Cart> cartList);
    ResponseBody<Map<String, Object>> getCartByIdList(List<Integer> idList);
}
