package com.bot.errorrobot.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class Result {

    private int code;

    private String message;

    private Object data;

    public static Result success(String message){
        return Result.builder().
                code(0).
                message(message).
                build();
    }

    public static Result failure(String message){
        return Result.builder().
                code(1).
                message(message).
                build();
    }

    public static Result successTrans(String message, Object data){
        return Result.builder().
                code(0).
                message(message).
                data(data).
                build();
    }
}
