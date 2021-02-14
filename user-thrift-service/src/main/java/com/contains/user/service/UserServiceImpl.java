package com.contains.user.service;

import com.contains.thrift.user.UserInfo;
import com.contains.thrift.user.UserService;
import com.contains.user.mapper.UserMapper;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date: 2021-02-13 18:18
 **/
@Service
public class UserServiceImpl implements UserService.Iface {
    @Autowired
    private UserMapper userMapper;

    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getTeacherById(int id) throws TException {
        return userMapper.gegetTeacherById(id);
    }

    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByName(username);
    }

    public void regiserUser(UserInfo userInfo) throws TException {
        userMapper.registerUser(userInfo);
    }
}
