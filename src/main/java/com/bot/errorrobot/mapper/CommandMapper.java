package com.bot.errorrobot.mapper;

import com.bot.errorrobot.pojo.UserCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@SuppressWarnings("all")
public interface CommandMapper {

    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "command",column = "command"),
            @Result(property = "userId",column = "user_id"),
            @Result(property = "commandContent",column = "command_content",javaType = String.class)
    })
    @Select("select id,command,user_id,command_content from user_command where user_id=#{userId}")
    List<UserCommand> queryCommand(Integer userId);
}
