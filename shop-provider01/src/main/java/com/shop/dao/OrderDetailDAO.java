package com.shop.dao;

import com.shop.bean.OrderDetail;
import com.shop.bean.OrderDetailExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderDetailDAO继承基类
 */
@Repository
public interface OrderDetailDAO extends MyBatisBaseDao<OrderDetail, Integer, OrderDetailExample> {
    Integer insertBatch(@Param("orderDetailList") List<OrderDetail> orderDetailList);
}