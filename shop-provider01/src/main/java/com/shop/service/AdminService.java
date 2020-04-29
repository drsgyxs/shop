package com.shop.service;

import com.shop.bean.Admin;
import com.shop.bean.ResponseBody;

public interface AdminService {
    ResponseBody login(Admin admin);
    ResponseBody addAdmin(Admin admin);
    ResponseBody getInfo(Integer id);
}
