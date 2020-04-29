package com.shop.dao;

import com.shop.bean.User;
import com.shop.bean.UserExample;
import org.springframework.stereotype.Repository;

/**
 * UserDAO继承基类
 */
@Repository
public interface UserDAO extends MyBatisBaseDao<User, Integer, UserExample> {
}