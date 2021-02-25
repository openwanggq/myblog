package com.wpy.controller;

import com.wpy.bean.RespBean;
import com.wpy.bean.User;
import com.wpy.service.AuthService;
import com.wpy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sang on 2017/12/17.
 */
@RestController
public class LoginRegController {

    @Autowired
    UserService userService;

    @Autowired
    private AuthService authService;

    /**
     * 登录
     */
    @PostMapping(value = "/login")
    public String login(String username, String password) throws AuthenticationException {
        // 登录成功会返回Token给用户
        return authService.login(username, password);
    }

    /**
     * 退出登录
     */
    @PostMapping(value = "/logout")
    public RespBean logout(String username, String password) throws AuthenticationException {
        // 登录成功会返回Token给用户
        SecurityContextHolder.getContext().setAuthentication(null);
        return new RespBean("success", "退出登录!");
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public RespBean reg(User user) {
        int result = userService.reg(user);
        if (result == 0) {
            //成功
            return new RespBean("success", "注册成功!");
        } else if (result == 1) {
            return new RespBean("error", "用户名重复，注册失败!");
        } else {
            //失败
            return new RespBean("error", "注册失败!");
        }
    }


}
