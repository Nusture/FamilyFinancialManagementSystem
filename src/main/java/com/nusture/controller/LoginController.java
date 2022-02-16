package com.nusture.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.nusture.pojo.User;
import com.nusture.service.FamilyService;
import com.nusture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private FamilyService familyService;
    @PostMapping("doLogin")
    public SaResult doLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.getUserByName(username);
        if (user != null) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                //设置token过期时间为7天
                StpUtil.login(user.getId(), new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
                return SaResult.data(StpUtil.getTokenInfo());
            }
        }
        System.out.println("用户名：" + username);
        System.out.println("密码：" + password);
        return SaResult.error("登录失败");
    }

    @PostMapping("getUsername")
    public SaResult getUsername(String username) {
        if (userService.getUserByName(username) == null) {
            return SaResult.ok();
        } else {
            return SaResult.error();
        }
    }

    @PostMapping("doRegister")
    public SaResult doRegister(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phone") String phone, @RequestParam("familyCode") String familyCode) {
        HashMap<String, String> map = new HashMap<>();
        BigDecimal money = new BigDecimal(0);
        if (userService.getUserByName(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setFamilyCode(familyCode);
            user.setVersion(1);
            user.setSignature("快来设置你的个性签名吧！");
            userService.registerUser(user);
            familyService.addBaseMoney(familyCode,money);
            map.put("msg", "注册成功！");
            return SaResult.data(map);
        } else {
            map.put("msg", "用户已存在！");
            return SaResult.data(map);
        }

    }

    // 查询Token
    @GetMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 查询登录状态
    @GetMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 注销
    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
