package com.shop.dao;

import com.shop.bean.Order;
import com.shop.bean.OrderExample;
import com.shop.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * OrderDAO继承基类
 */
@Repository
public interface OrderDAO extends MyBatisBaseDao<Order, Long, OrderExample> {
    List<Map<String, String>> spendRank(@Param("offset") Long offset, @Param("limit") Integer limit);
    List<Map<String, String>> orderQuantityRank(@Param("offset") Long offset, @Param("limit") Integer limit);
}