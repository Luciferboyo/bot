package com.bot.errorrobot.service.impl;

import com.bot.errorrobot.mapper.UserMapper;
import com.bot.errorrobot.pojo.User;
import com.bot.errorrobot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(String username, String token) {
        return userMapper.queryUserWithUT(username,token);
    }

    @Override
    public User reInspection(String username, String token) {
        String  nameInData= userMapper.queryUserWUsername(username);
        String tokenInData = userMapper.queryUserWToken(token);
        return User.builder().
                    username(nameInData).
                    token(tokenInData).
                    build();
    }

    @Override
    public void register(String username, String token) {
        userMapper.addUsernameAndToken(username,token);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }


}
