package com.bot.errorrobot.component;

import com.bot.errorrobot.mapper.UserMapper;
import com.bot.errorrobot.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class TelegramSender {

    public static ErrorReportingRobot errorReportingRobot;
    private boolean isInitialized = false;



    @Autowired
    private UserMapper userMapper;

    public void init(Integer id){

        if (isInitialized){
            throw new RuntimeException("机器人已经初始化过了");
        }

        User user = userMapper.queryGeneralUser(id);

        try {
            DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            errorReportingRobot = ErrorReportingRobot.getInstance(defaultBotOptions,user.getUsername(), user.getToken());
            botsApi.registerBot(errorReportingRobot);
            isInitialized = true;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutDown(){

        errorReportingRobot.onClosing();

    }
}
