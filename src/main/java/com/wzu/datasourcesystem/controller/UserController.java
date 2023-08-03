package com.wzu.datasourcesystem.controller;


import com.google.code.kaptcha.Producer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzu.datasourcesystem.common.R;
import com.wzu.datasourcesystem.config.MailUtils;
import com.wzu.datasourcesystem.mapper.UserMapper;
import com.wzu.datasourcesystem.pojo.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserMapper mapper;
    private String code;
    @PostMapping("/login")
    public R<Users> login(@RequestBody Users user){
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        Users u = mapper.selectOne(wrapper);
        if(u == null){
            return R.error("用户不存在");
        }
        if(u.getPassword().equals(user.getPassword())){
            return R.success(user);
        }
       return R.error("用户密码错误");
    }
    @PostMapping("/register")
    public R<Users> register(@RequestBody Users user){

        log.info(code);
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        Users u = mapper.selectOne(wrapper);
        if( u != null){
            return R.error("用户已经存在");
        }
        if(!user.getCode().equals(code)){
            return R.error("验证码错误");
        }
        mapper.insert(user);
        return R.success(mapper.selectOne(wrapper));
    }

    @Autowired
    private MailUtils mailUtils;
    @Autowired
    private Producer checkCode;
    @PostMapping("/sendEmail")
    public R<Users> sendEmail(@RequestBody Users user){
        String text = checkCode.createText();
        Users u = new Users();
        code = text;
        u.setCode(text);
        System.out.println("text"+text);
        String s="您注册用户"+user.getEmail()+"所需的验证码是"+text;
        mailUtils.sendMail(user.getEmail(),s,"邮件发送测试");
        return R.success(u);
    }
}
