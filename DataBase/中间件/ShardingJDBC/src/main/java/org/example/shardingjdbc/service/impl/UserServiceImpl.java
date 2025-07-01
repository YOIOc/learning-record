package org.example.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.shardingjdbc.domain.User;
import org.example.shardingjdbc.service.UserService;
import org.example.shardingjdbc.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
}




