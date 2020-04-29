package com.shop.controller;

import com.shop.bean.Cart;
import com.shop.bean.ResponseBody;
import com.shop.service.CartService;
import com.shop.validate.Insert;
import com.shop.validate.Select;
import com.shop.validate.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class CartController {
    private final CartService service;

    @Autowired
    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/cart")
    public ResponseBody<List<Cart>> getCartList(Integer userId) {
        return service.getCartList(userId);
    }

    @GetMapping("/cart/count")
    public ResponseBody<Long> countCart(Integer userId) {
        return service.countCart(userId);
    }

    @PostMapping("/cart")
    public ResponseBody<Integer> addCart(Integer userId, @Validated(Insert.class) @RequestBody Cart cart) {
        return service.addCart(userId, cart);
    }

    @PutMapping("/cart")
    public ResponseBody<Integer> updateCart(@Validated(Update.class) @RequestBody Cart cart) {
        return service.updateCart(cart);
    }

    @DeleteMapping("/cart")
    public ResponseBody<Integer> multiDeleteCart(@NotEmpty(message = "购物车id不能为空") @RequestParam("list") List<Integer> cartIdList) {
        return service.multiDeleteCart(cartIdList);
    }

    @GetMapping("/cart/idList")
    public ResponseBody<Map<String, Object>> getCartByIdList(@NotEmpty(message = "请选择购物车商品后再试") @RequestParam("list") List<Integer> idList) {
        return service.getCartByIdList(idList);
    }

    @DeleteMapping("/cartAll")
    public ResponseBody<Integer> deleteAllCart(Integer userId) {
        return service.deleteAllCart(userId);
    }

    @GetMapping("/cart/price")
    public ResponseBody<BigDecimal> getTotalPrice(List<Cart> cartList) {
        return service.getTotalPrice(cartList);
    }
}
