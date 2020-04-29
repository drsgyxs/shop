package com.shop.service.impl;

import com.shop.bean.*;
import com.shop.dao.CommodityTypeDAO;
import com.shop.service.CommodityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityTypeServiceImpl implements CommodityTypeService {
    private final CommodityTypeDAO dao;

    @Autowired
    public CommodityTypeServiceImpl(CommodityTypeDAO dao) {
        this.dao = dao;
    }

    @Override
    public ResponseBody<List<CommodityType>> getCommodityTypeLevelList() {
        return new ResponseBody<>(dao.selectLevelByExample(new CommodityTypeExample()));
    }

    @Override
    public ResponseBody addType(CommodityType type) {
        if (type.getPid() == null)
            type.setPid(0);
        return ResponseBody.success(dao.insert(type));
    }

    @Override
    public ResponseBody updateType(CommodityType type) {
        if (type.getPid() == null)
            type.setPid(0);
        return ResponseBody.success(dao.updateByPrimaryKeySelective(type));
    }

    @Override
    public ResponseBody delType(Integer id) {
        return ResponseBody.success(dao.deleteByPrimaryKey(id));
    }

    @Override
    public ResponseBody getTypeList(Integer level) {
        CommodityTypeExample example = new CommodityTypeExample();
        CommodityTypeExample.Criteria criteria = example.createCriteria();
        if (level == 1)
            criteria.andPidEqualTo(0);
        else
            criteria.andPidGreaterThan(0);
        return ResponseBody.success(dao.selectByExample(example));
    }
}
