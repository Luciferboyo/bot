package com.bot.errorrobot.pojo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCommand {

    private Integer id;

    private String command;

    private Integer userId;

    private String commandContent;

}
