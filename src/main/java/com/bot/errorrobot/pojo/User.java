package com.bot.errorrobot.pojo;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class User {

    private Integer id;

    private String token;

    private String username;

}
