package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    @NotNull // 值不能为null
    private Integer id;//主键ID

    private String username;//用户名

    @JsonIgnore // 当该对象转换成Json格式输出给前端时，忽略password字段的展示
    private String password;//密码

    @NotEmpty // 值不能为null，且能容不能为空
    @Pattern(regexp = "^\\S{1,10}$") // 1~10个字符，非空
    private String nickname;//昵称

    @NotEmpty
    @Email // 满足邮箱格式
    private String email;//邮箱

    private String userPic;//用户头像地址

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
}
