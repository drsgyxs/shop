package com.shop.controller;

import com.shop.bean.Address;
import com.shop.bean.ResponseBody;
import com.shop.service.AddressService;
import com.shop.validate.Insert;
import com.shop.validate.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class AddressController {
    private final AddressService service;

    @Autowired
    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping("/address")
    public  ResponseBody<List<Address>> getAddressList(Integer userId) {
        return service.getAddressList(userId);
    }

    @PostMapping("/address")
    public ResponseBody<Integer> addAddress(Integer userId, @Validated(Insert.class) @RequestBody Address address) {
        return service.addAddress(userId, address);
    }

    @PatchMapping("/address")
    public ResponseBody<Integer> updateAddress(@Validated({Update.class, Insert.class}) @RequestBody Address address) {
        return service.updateAddress(address);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseBody<Integer> deleteAddress(@PathVariable("addressId") Integer addressId) {
        return service.deleteAddress(addressId);
    }

    @PatchMapping("/address/{addressId}")
    public ResponseBody<Integer> setDefault(Integer userId, @PathVariable("addressId") Integer AddressId) {
        return service.setDefault(userId, AddressId);
    }
}
