package com.shop.dao;

import com.shop.bean.Commodity;
import com.shop.bean.CommodityExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * CommodityDAO继承基类
 */
@Repository
public interface CommodityDAO extends MyBatisBaseDao<Commodity, Integer, CommodityExample> {
//    @CacheEvict(value = "commodity", allEntries = true)
    Integer updateList(@Param("commodityList") List<Commodity> commodityList);
}