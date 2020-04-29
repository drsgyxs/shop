package com.shop.dao;

import com.shop.bean.CommodityType;
import com.shop.bean.CommodityTypeExample;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CommodityTypeDAO继承基类
 */
@Repository
public interface CommodityTypeDAO extends MyBatisBaseDao<CommodityType, Integer, CommodityTypeExample> {
    List<CommodityType> selectLevelByExample(CommodityTypeExample example);
}