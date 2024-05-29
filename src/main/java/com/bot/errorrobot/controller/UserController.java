package com.bot.errorrobot.controller;

import com.bot.errorrobot.pojo.Result;
import com.bot.errorrobot.pojo.User;
import com.bot.errorrobot.service.UserService;
import com.bot.errorrobot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/login")
    public Result login(String username, String token){

        if (username == null || token == null){
            return Result.failure("username或者token不能为空");
        }

        User loginUser = userService.login(username, token);

        if (loginUser == null){
            return Result.failure("这个机器人不存在,输入的username或者token有错");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username",loginUser.getUsername());
        claims.put("token",loginUser.getToken());
        claims.put("id",loginUser.getId());
        String jwt = JwtUtil.genToken(claims);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(jwt,jwt,12, TimeUnit.HOURS);

        return Result.successTrans("登录成功",jwt);
    }

    @PostMapping("/register")
    public Result register(String username,String token){

        if (Objects.isNull(username) || Objects.isNull(token)){
            return Result.failure("username或者token不能为空");
        }

        User userInData = userService.reInspection(username, token);

        if (userInData.getUsername() != null || userInData.getToken() != null){
            return Result.failure("username或者token已经存在");
        }

        userService.register(username,token);

        return  Result.success("注册成功");
    }
    @PutMapping("/updateUsernameToken")
    public Result updateUsernameToken(@RequestBody User user){

        User userInData = userService.reInspection(user.getUsername(), user.getToken());

        if (userInData.getUsername() != null || userInData.getToken() != null){
            return Result.failure("username或者token已经存在");
        }

        userService.updateUser(user);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result deleteUser(Integer id){
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}
