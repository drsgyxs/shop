package com.shop.service;

import com.shop.bean.Address;
import com.shop.bean.ResponseBody;
import java.util.List;

public interface AddressService {
    ResponseBody<List<Address>> getAddressList(Integer userId);
    Address getAddressById(Integer addressId);
    ResponseBody<Integer> addAddress(Integer userId, Address address);
    ResponseBody<Integer> updateAddress(Address address);
    ResponseBody<Integer> deleteAddress(Integer addressId);
    ResponseBody<Integer> setDefault(Integer userId, Integer addressId);
}
