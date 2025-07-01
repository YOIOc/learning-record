package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController /* 被标注的类将作为Controller，并且使类中方法的返回值修改为josn格式 */
@RequestMapping("/user") /* 为方法路径添加前缀 */
@Validated /* 被标注后可对方法参数进行校验 */
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/register") /* 若请求体中键值对的key与形参名一致，将自动传值 */
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        // 查询当前注册的用户是否存在
        User u = userService.findByUserName(username);
        if (u == null) {
            // 没有注册-注册
            userService.register(username, password);
            return Result.success();
        } else {
            // 已被注册
            return Result.error("该用户名已被使用");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$")String password){
        // 根据用户名查询User
        User u = userService.findByUserName(username);
        // 判断是否查询到
        if (u == null) {
            // 用户不存在
            return Result.error("用户不存在");
        } else if (!Md5Util.getMD5String(password).equals(u.getPassword())) {
            // 密码错误
            return Result.error("密码错误");
        }
        // 密码正确(为用户生成Token)
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", u.getId());
        claims.put("username", u.getUsername());
        String token = JwtUtil.genToken(claims);
        // 将token存储到redis缓存中
        redisTemplate.opsForValue().set(token, token, 12, TimeUnit.HOURS);
        return Result.success(token);
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        // 根据用户名查询用户
        Map<String, Object> claims = (Map<String, Object>) ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){ // 使用@RequestBode注解将前端Json格式数据，自动封装进对应实体类
        if (userService.findByUserName(user.getUsername()) == null) {
            return Result.error("用户不存在");
        }
        userService.update(user);
        return Result.success("信息修改成功");
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success("头像更新成功");
    }
    
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token){
        // 校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        System.out.println(newPwd);
        // 1.参数是否齐全
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数");
        }

        // 2.原密码是否正确/修改后的密码是否一致
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        String username = (String) userInfo.get("username");
        if (!Md5Util.getMD5String(oldPwd).equals(userService.findByUserName(username).getPassword())) {
            // 原密码错误
            return Result.error("原密码错误");
        } else if(!newPwd.matches("^\\S{5,16}$") || !rePwd.matches("^\\S{5,16}$")){
            // 新密码格式不正确
            return Result.error("新密码格式非法(5~16个字符)");
        } else if (!newPwd.equals(rePwd)) {
            // 密码填写不一致
            return Result.error("密码填写不一致");
        }
        // 修改密码
        userService.updatePwd(newPwd);
        // 删除Redis缓存中的Token
        redisTemplate.delete(token);
        return Result.success("修改成功");
    }
}
