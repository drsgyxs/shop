package com.shop.service.impl;

import com.shop.bean.Admin;
import com.shop.bean.AdminExample;
import com.shop.bean.ResponseBody;
import com.shop.dao.AdminDAO;
import com.shop.exception.BasicException;
import com.shop.service.AdminService;
import com.shop.util.TokenUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminDAO dao;

    @Override
    public ResponseBody login(Admin admin) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(admin.getName()).andPasswordEqualTo(admin.getPassword());
        List<Admin> admins = dao.selectByExample(example);
        Map<String, String> map = new HashMap<>(1);
        if (admins.isEmpty())
            throw new BasicException("用户名或密码错误");
        else {
            Admin info = admins.get(0);
            String token = TokenUtil.sign(info.getId(), info.getName());
            if (token == null)
                throw new BasicException("生成token出错");
            map = new HashMap<>(1);
            map.put("token", token);
        }
        return ResponseBody.success(map);
    }

    @Override
    public ResponseBody addAdmin(Admin admin) {
        AdminExample example = new AdminExample();
        AdminExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(admin.getName());
        List<Admin> admins = dao.selectByExample(example);
        if (!admins.isEmpty())
            throw new BasicException("用户名已被占用");
        return ResponseBody.success(dao.insertSelective(admin));
    }

    @Override
    public ResponseBody getInfo(Integer id) {
        return ResponseBody.success(dao.selectByPrimaryKey(id));
    }
}
