package org.example.shardingjdbc.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@TableName(value ="user")
@Data
public class User implements Serializable {
    @TableId
    private Long id;

    private String nickname;

    private String password;

    private Integer sex;

    private Date birthday;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}