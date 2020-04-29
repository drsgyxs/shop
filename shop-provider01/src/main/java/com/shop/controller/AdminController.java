package com.shop.controller;

import com.shop.bean.Admin;
import com.shop.bean.ResponseBody;
import com.shop.service.AdminService;
import com.shop.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Validated
public class AdminController {
    @Resource
    private AdminService service;

    @PostMapping("/admin/login")
    public ResponseBody login(@RequestBody Admin admin) {
        return service.login(admin);
    }

    @PostMapping("/admin")
    public ResponseBody addAdmin(@RequestBody Admin admin) {
        return service.addAdmin(admin);
    }

    @GetMapping("/admin")
    public ResponseBody getInfo(String token) {
        Integer id = TokenUtil.getId(token);
        return service.getInfo(id);
    }

}
