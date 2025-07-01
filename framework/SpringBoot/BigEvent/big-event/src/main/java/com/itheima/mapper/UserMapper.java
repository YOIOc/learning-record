package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    // 根据用户名查询
    @Select("select * from user where username = #{username}")
    User findByUserName(String username);

    // 添加用户
    @Insert("insert into user(username, password, create_time, update_time) values (#{username}, #{password}, now(), now())")
    void add(String username, String password);

    // 修改基本用户信息
    @Update("update user set nickname=#{nickname}, email=#{email}, update_time=now() where id=#{id}")
    void update(User user);

    // 更新用户头像
    @Update("update user set user_pic=#{avatarUrl}, update_time=now() where id=#{id}")
    void updateAvater(String avatarUrl, Integer id);

    // 修改用户密码
    @Update("update user set password=#{md5String}, update_time=now() where id=#{id}")
    void updatePwd(String md5String, Integer id);

}
