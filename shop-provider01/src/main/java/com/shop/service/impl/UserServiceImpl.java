package com.shop.service.impl;

import com.shop.bean.ResponseBody;
import com.shop.bean.User;
import com.shop.bean.UserExample;
import com.shop.dao.UserDAO;
import com.shop.exception.BasicException;
import com.shop.service.UserService;
import com.shop.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO dao;
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${spring.mail.username}")
    private String from;
//    private String namePattern = "^.{6,16}$";
//    private String pwdPattern = "^(?=.*[A-Za-z_])[A-Za-z\\d_]{6,20}$";
//    private String pwdPattern = "^[A-Za-z\\d_]{8,16}$";
//    private String mailPattern = "^[a-zA-Z0-9_-]{1,50}@[a-zA-Z0-9_-]{1,20}(\\.[a-zA-Z0-9_-]+)+$";

    @Autowired
    public UserServiceImpl(UserDAO dao, JavaMailSender mailSender, RedisTemplate<String, String> redisTemplate) {
        this.dao = dao;
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ResponseBody<Integer> register(User user, String code) {
        ResponseBody<Integer> res = new ResponseBody<>();
        if (code.equals(redisTemplate.opsForValue().get(user.getMail()))) {
            res.setData(dao.insert(user));
            redisTemplate.opsForValue().set(user.getMail(), "invalid");
        } else
            throw new BasicException("验证码错误");
        return res;
    }

    @Override
    public ResponseBody sendCode(String mail) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andMailEqualTo(mail);
        List<User> users = dao.selectByExample(userExample);
        if (!users.isEmpty())
            throw new BasicException("邮箱已被注册");
        else {
            int code = (int) ((Math.random() * 9 + 1) * 100000);
            new Thread(() -> {
                MimeMessage message = mailSender.createMimeMessage();
                redisTemplate.opsForValue().set(mail, String.valueOf(code), 5, TimeUnit.MINUTES);
                try {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                    messageHelper.setTo(mail);
                    messageHelper.setFrom(from, "Hopsop");
                    messageHelper.setSubject("验证");
                    messageHelper.setText("验证码为：" + code + "。", false);
                    mailSender.send(message);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return ResponseBody.success(null);
    }

    @Override
    public ResponseBody getResetCode(String mail) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andMailEqualTo(mail);
        List<User> users = dao.selectByExample(userExample);
        if (users.isEmpty()) {
            throw new BasicException("账号不存在");
        } else {
            int code = (int) ((Math.random() * 9 + 1) * 100000);
            new Thread(() -> {
                MimeMessage message = mailSender.createMimeMessage();
                redisTemplate.opsForValue().set(mail, String.valueOf(code), 5, TimeUnit.MINUTES);
                try {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                    messageHelper.setTo(mail);
                    messageHelper.setFrom(from, "Hopsop");
                    messageHelper.setSubject("重置密码");
                    messageHelper.setText("验证码为：" + code + "。", false);
                    mailSender.send(message);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return ResponseBody.success(null);
    }

    @Override
    public ResponseBody resetPassword(Map<String, String> map) {
        String mail = map.get("mail");
        String inputCode = map.get("code");
        String password = map.get("password");
        String code = redisTemplate.opsForValue().get(mail);
        if (!inputCode.equals(code))
            throw new BasicException("验证码错误");
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andMailEqualTo(mail);
        User user = new User();
        user.setPassword(password);
        return ResponseBody.success(dao.updateByExampleSelective(user, example));
    }

    @Override
    public ResponseBody getUserList(Integer pageIndex, Integer pageSize) {
        UserExample userExample = new UserExample();
        userExample.setOffset((long) (pageIndex - 1) * pageSize);
        userExample.setLimit(pageSize);
        long total = dao.countByExample(userExample);
        List<User> users = dao.selectByExample(userExample);
        Map<String, Object> map = new HashMap<>(1);
        map.put("total", total);
        map.put("items", users);
        return ResponseBody.success(map);
    }

    @Override
    public ResponseBody updateUser(User user) {
        return ResponseBody.success(dao.updateByPrimaryKeySelective(user));
    }

    @Override
    public ResponseBody delUser(Integer id) {
        return ResponseBody.success(dao.deleteByPrimaryKey(id));
    }

    @Override
    public ResponseBody<String> login(User user) {
        ResponseBody<String> res = new ResponseBody<>();
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andMailEqualTo(user.getMail()).andPasswordEqualTo(user.getPassword());
        List<User> users = dao.selectByExample(userExample);
        if (!users.isEmpty()) {
            String token = TokenUtil.sign(users.get(0).getId(), users.get(0).getMail());
            res.setData(token);
            if (token == null)
                throw new BasicException("生成token出错");
        } else
            throw new BasicException("用户名或密码错误");
        return res;
    }

    @Override
    public ResponseBody<Integer> updatePwd(Integer userId, Map<String, String> map) {
        ResponseBody<Integer> res = new ResponseBody<>();
        User user = dao.selectByPrimaryKey(userId);
        if (user.getPassword().equals(map.get("oldPwd"))) {
            if (map.get("newPwd").equals(map.get("pwdConfirm"))) {
                user.setPassword(map.get("newPwd"));
                res.setData(dao.updateByPrimaryKeySelective(user));
            } else
                throw new BasicException("两次密码不一致");
        } else
            throw new BasicException("旧密码有误");
        return res;
    }

    @Override
    public ResponseBody getUserByMail(String mail) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andMailEqualTo(mail);
        return ResponseBody.success(dao.selectByExample(example));
    }

    @Override
    public ResponseBody<User> getUser(Integer userId) {
        return ResponseBody.success(dao.selectByPrimaryKey(userId));
    }

    @Override
    public ResponseBody<Integer> updateMail(Integer userId, String mail, String code) {
        ResponseBody<Integer> res = new ResponseBody<>();
        if (code.equals(redisTemplate.opsForValue().get(mail))) {
            User user = new User();
            user.setId(userId);
            user.setMail(mail);
            res.setData(dao.updateByPrimaryKeySelective(user));
        } else {
            throw new BasicException("验证码错误");
        }
        return res;
    }
}