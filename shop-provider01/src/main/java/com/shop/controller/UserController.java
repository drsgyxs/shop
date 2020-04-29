package com.shop.controller;

import com.shop.bean.ResponseBody;
import com.shop.bean.User;
import com.shop.service.UserService;
import com.shop.util.TokenUtil;
import com.shop.validate.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

@RestController
@Validated
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping("/user/code/{mail}")
    public ResponseBody sendCode(@NotBlank(message = "邮箱不能为空")
                                 @Pattern(message = "邮箱不合法", regexp = "^[a-zA-Z0-9_-]{1,50}@[a-zA-Z0-9_-]{1,20}(\\.[a-zA-Z0-9_-]+)+$")
                                 @PathVariable("mail") String mail) {
        return service.sendCode(mail);
    }

    @PostMapping("/register")
    public ResponseBody register(@RequestBody User user, @RequestParam("code") String code) {
        return service.register(user, code);
    }

    @GetMapping("/login")
    public ResponseBody<String> login(@Validated(Select.class) User user) {
        return service.login(user);
    }

    @PutMapping("/user/password")
    public ResponseBody<Integer> updatePassword(Integer userId, @RequestBody Map<String, String> map) {
        return service.updatePwd(userId, map);
    }

    @PutMapping("/user/{mail}")
    public ResponseBody<Integer> updateMail(Integer userId,
                                            @PathVariable("mail") String mail,
                                            @NotBlank(message = "验证码不能为空") @RequestBody String code) {
        return service.updateMail(userId, mail, code);
    }

    @GetMapping("/user")
    public ResponseBody<User> getUser(Integer userId) {
        return service.getUser(userId);
    }

    @GetMapping("/is_login")
    public ResponseBody<Boolean> isLogin(@RequestHeader(value = "token", required = false) String token) {
        return ResponseBody.success(TokenUtil.verify(token));
    }

    @GetMapping("/user/resetpassword/code/{mail}")
    public ResponseBody getResetCode(@PathVariable("mail") String mail) {
        return service.getResetCode(mail);
    }

    @PutMapping("/user/resetpassword")
    public ResponseBody resetPassword(@RequestBody Map<String, String> map) {
        return service.resetPassword(map);
    }

    @GetMapping("/admin/users")
    public ResponseBody getUserList(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return service.getUserList(pageIndex, pageSize);
    }

    @PutMapping("/admin/user")
    public ResponseBody updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("/admin/user/{id}")
    public ResponseBody delUser(@PathVariable("id") Integer id) {
        return service.delUser(id);
    }

    @GetMapping("/admin/user/{mail}")
    public ResponseBody getUserByMail(@PathVariable("mail") String mail) {
        return service.getUserByMail(mail);
    }

}
