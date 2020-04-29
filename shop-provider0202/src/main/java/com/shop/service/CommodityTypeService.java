package com.shop.service;

import com.shop.bean.CommodityType;
import com.shop.bean.ResponseBody;

import java.util.List;

public interface CommodityTypeService {
    ResponseBody<List<CommodityType>> getCommodityTypeLevelList();
    ResponseBody addType(CommodityType type);
    ResponseBody updateType(CommodityType type);
    ResponseBody delType(Integer id);
    ResponseBody getTypeList(Integer level);
}
