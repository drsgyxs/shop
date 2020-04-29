package com.shop.service;


import com.shop.bean.ResponseBody;
import com.shop.bean.User;
import java.util.Map;

public interface UserService {
    ResponseBody<Integer> register(User user, String code);
    ResponseBody<String> login(User user);
    ResponseBody<Integer> updatePwd(Integer userId, Map<String, String> map);
    ResponseBody<User> getUser(Integer userId);
    ResponseBody<Integer> updateMail(Integer userId, String mail, String code);
    ResponseBody sendCode(String mail);
    ResponseBody getResetCode(String mail);
    ResponseBody resetPassword(Map<String, String> map);
    ResponseBody getUserList(Integer pageIndex, Integer pageSize);
    ResponseBody updateUser(User user);
    ResponseBody delUser(Integer id);
    ResponseBody getUserByMail(String mail);
}
