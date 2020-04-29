package com.shop.dao;

import com.shop.bean.Admin;
import com.shop.bean.AdminExample;
import org.springframework.stereotype.Repository;

/**
 * AdminDAO继承基类
 */
@Repository
public interface AdminDAO extends MyBatisBaseDao<Admin, Integer, AdminExample> {
}