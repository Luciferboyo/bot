package com.bot.errorrobot.utils;

import java.util.Map;

public class TypeConversionUtil {
    public static Integer getIntegerId(Map<String,Object> map){
        if (map == null){
            throw new IllegalArgumentException("Map cannot be null");
        }
        Object idObj = map.get("id");
        if(idObj instanceof Integer){
            return (Integer) idObj;
        }else {
            throw new RuntimeException("Invalid id value: " + idObj);
        }
    }
}
