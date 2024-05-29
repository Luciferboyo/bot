package com.bot.errorrobot.controller;

import com.bot.errorrobot.component.TelegramSender;
import com.bot.errorrobot.pojo.Result;
import com.bot.errorrobot.utils.ThreadLocalUtil;
import com.bot.errorrobot.utils.TypeConversionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    @Autowired
    private TelegramSender telegramSender;

    @GetMapping("/open")
    public Result openBot(){

        //ThreadUtils
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = TypeConversionUtil.getIntegerId(map);

        /*Integer id = null;
        Map<String,Object> map = ThreadLocalUtil.get();
        Object idObj = map.get("id");
        if (idObj instanceof Integer){
            id =  (Integer) idObj;
        }*/
        telegramSender.init(id);
        return Result.success("机器人设置完成,您可以使用了telegramBot了");
    }

    @GetMapping("/shutDown")
    public Result shutBot(){
        telegramSender.shutDown();
        return Result.success("机器人已经关闭");
    }
}
