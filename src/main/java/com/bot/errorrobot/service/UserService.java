package com.bot.errorrobot.service;

import com.bot.errorrobot.pojo.User;

public interface UserService {

    User login(String username,String password);

    User reInspection(String username,String token);

    void register(String username,String token);

    void updateUser(User user);

    void deleteUser(Integer id);
}
