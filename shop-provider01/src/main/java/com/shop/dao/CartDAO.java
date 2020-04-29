package com.shop.dao;

import com.shop.bean.Cart;
import com.shop.bean.CartExample;
import org.springframework.stereotype.Repository;

/**
 * CartDAO继承基类
 */
@Repository
public interface CartDAO extends MyBatisBaseDao<Cart, Integer, CartExample> {
}