package com.shop.dao;

import com.shop.bean.Address;
import com.shop.bean.AddressExample;
import org.springframework.stereotype.Repository;

/**
 * AddressDAO继承基类
 */
@Repository
public interface AddressDAO extends MyBatisBaseDao<Address, Integer, AddressExample> {
}