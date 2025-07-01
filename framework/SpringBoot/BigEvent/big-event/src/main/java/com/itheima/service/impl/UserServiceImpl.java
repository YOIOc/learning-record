package com.itheima.service.impl;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(String username, String password) {
        // 加密
        String md5String = Md5Util.getMD5String(password);
        userMapper.add(username, md5String);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = (Map<String, Object>) ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvater(avatarUrl, id);
    }

    @Override
    public void updatePwd(String newPwd) {
        // 加密
        String md5String = Md5Util.getMD5String(newPwd);
        // 获取用户id
        Map<String, Object> userInfo = (Map<String, Object>) ThreadLocalUtil.get();
        Integer id = (Integer) userInfo.get("id");
        userMapper.updatePwd(md5String, id);
    }
}
