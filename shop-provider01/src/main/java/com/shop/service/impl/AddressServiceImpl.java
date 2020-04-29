package com.shop.service.impl;

import com.shop.bean.Address;
import com.shop.bean.AddressExample;
import com.shop.bean.ResponseBody;
import com.shop.dao.AddressDAO;
import com.shop.exception.BasicException;
import com.shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressDAO dao;
    private final long MaxAddressQuantity = 20;

    @Autowired
    public AddressServiceImpl(AddressDAO dao) {
        this.dao = dao;
    }

    @Override
    public  ResponseBody<List<Address>> getAddressList(Integer userId) {
        ResponseBody<List<Address>> res = new ResponseBody<>();
        AddressExample addressExample = new AddressExample();
        AddressExample.Criteria criteria = addressExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        addressExample.setOrderByClause("is_default desc");
        res.setData(dao.selectByExample(addressExample));
        return res;
    }

    @Override
    public Address getAddressById(Integer addressId) {
        return dao.selectByPrimaryKey(addressId);
    }

    @Override
    public ResponseBody addAddress(Integer userId, Address address) {
        if (countAddress(userId) < MaxAddressQuantity) {
            address.setUserId(userId);
            if (address.getIsDefault() == 1) {
                cancelAllDefault(userId);
            }
            return new ResponseBody<>(dao.insert(address));
        } else
            throw new BasicException("最多只能添加20个收货地址");
    }

    @Override
    public ResponseBody<Integer> updateAddress(Address address) {
        if (address.getIsDefault() == 1)
            cancelAllDefault(address.getUserId());
        return new ResponseBody<>(dao.updateByPrimaryKeySelective(address));
    }

    @Override
    public ResponseBody<Integer> deleteAddress(Integer addressId) {
        return new ResponseBody<>(dao.deleteByPrimaryKey(addressId));
    }

    @Override
    public ResponseBody<Integer> setDefault(Integer userId, Integer addressId) {
        cancelAllDefault(userId);
        Address address = new Address();
        address.setId(addressId);
        address.setIsDefault(1);
        return new ResponseBody<>(dao.updateByPrimaryKeySelective(address));
    }

    private Integer cancelAllDefault(Integer userId) {
        AddressExample addressExample = new AddressExample();
        AddressExample.Criteria criteria = addressExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        Address address = new Address();
        address.setIsDefault(0);
        return dao.updateByExampleSelective(address, addressExample);
    }

    private long countAddress(Integer userId) {
        AddressExample addressExample = new AddressExample();
        AddressExample.Criteria criteria = addressExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return dao.countByExample(addressExample);
    }
}
