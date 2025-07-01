package org.example.shardingjdbc.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.shardingjdbc.domain.User;
import org.example.shardingjdbc.responses.Result;
import org.example.shardingjdbc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/save")
    public Result<User> insert(){
        User user = new User();
        user.setNickname("zhangsan");
        user.setPassword("123456");
        user.setSex(1);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            user.setBirthday(format.parse("2003-02-03"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        userService.save(user);
        return Result.succeed(user);
    }

    @GetMapping("/list")
    public Result<List<User>> listerUser(){
        return Result.succeed(userService.list());
    }
}
