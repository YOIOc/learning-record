package com.itheima.service;

import com.itheima.pojo.User;

public interface UserService {
    // 根据用户名查询
    User findByUserName(String username);

    // 注册
    void register(String username, String password);

    // 更新基本信息
    void update(User user);

    // 更新头像
    void updateAvatar(String avatarUrl);

    // 修改密码
    void updatePwd(String newPwd);
}
