package com.shop.dao;

import com.shop.bean.CommodityImage;
import com.shop.bean.CommodityImageExample;
import org.springframework.stereotype.Repository;

/**
 * CommodityImageDAO继承基类
 */
@Repository
public interface CommodityImageDAO extends MyBatisBaseDao<CommodityImage, Integer, CommodityImageExample> {
}