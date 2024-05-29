package com.bot.errorrobot.mapper;

import com.bot.errorrobot.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
@SuppressWarnings("all")
public interface UserMapper {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "token", column = "token"),
            @Result(property = "username", column = "username",javaType = String.class)
    })

    @Select("select * from user where id=#{id}")
    User queryGeneralUser(Integer id);

    @Select("select username from user where username=#{username}")
    String queryUserWUsername(String username);

    @Select("select token from user where token=#{token}")
    String queryUserWToken(String token);

    @Select("select * from user where username=#{username} and token=#{token}")
    User queryUserWithUT(String username,String token);

    @Insert("insert into user(username,token)" +(" values (#{username},#{token})"))
    void addUsernameAndToken(String username,String token);

    @Update("update user set username=#{username},token=#{token} where id=#{id}")
    void updateUser(User user);

    @Delete("delete from article where id=#{id}")
    void deleteUser(Integer id);
}
