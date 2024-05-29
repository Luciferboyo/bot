package com.bot.errorrobot.component;

import com.bot.errorrobot.mapper.CommandMapper;
import com.bot.errorrobot.mapper.UserMapper;
import com.bot.errorrobot.pojo.User;
import com.bot.errorrobot.pojo.UserCommand;
import com.bot.errorrobot.utils.ThreadLocalUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ErrorReportingRobot extends TelegramLongPollingBot {

    private String token;
    private String userName;
    private boolean isBotClosed = false;
    private static ErrorReportingRobot instance;
    public static ErrorReportingRobot errorReportingRobot;

    @Autowired
    private CommandMapper commandMapper;

    @Autowired
    private UserMapper userMapper;

    public User getMapperUser(){//static

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return errorReportingRobot.userMapper.queryGeneralUser(id);

    }

    @PostConstruct
    public void init(){

        errorReportingRobot = this;
        errorReportingRobot.commandMapper = this.commandMapper;
        errorReportingRobot.userMapper = this.userMapper;

    }

    public List<UserCommand> getMapperQueryCommand(){//static

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return errorReportingRobot.commandMapper.queryCommand(id);

    }

    private ErrorReportingRobot(DefaultBotOptions botOptions,String token,String userName){

        super(botOptions);
        this.token = token;
        this.userName = userName;

    }

    public static ErrorReportingRobot getInstance(DefaultBotOptions botOptions,String userName,String token){

        if (instance == null) {
            synchronized (ErrorReportingRobot.class) {
                if (instance == null) {
                    instance = new ErrorReportingRobot(botOptions, token, userName);
                }
            }
        }
        return instance;
    }

    @Override
    public String getBotUsername() {
        return this.userName;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }



    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            Message message = update.getMessage();

            //获得mapper
            List<UserCommand> userCommands = errorReportingRobot.getMapperQueryCommand();

            if (update.getMessage().getChat().getType().equals("private")){

                handlePrivateCommand(message,userCommands);

            }else{

                handleGroupCommand(message,userCommands);

            }

        }
    }

    @Override
    public void onClosing() {

       /* try {

            DeleteWebhook deleteWebhookRequest = new DeleteWebhook();

            Boolean result = execute(deleteWebhookRequest);
            if (result){
                log.info("Webhook deleted successfully");
            }else {
                log.error("Failed to delete webhook");
            }
            WebhookInfo webhookInfo = getWebhookInfo();
            log.info("Webhook info:{}",webhookInfo);

            isBotClosed = true;
            instance = null;
        } catch (TelegramApiException e) {
            log.error("Error while closing the bot: {}", e.getMessage());
        }*/
    }

    private void handlePrivateCommand(Message message, List<UserCommand> userCommands){

        StringBuffer errorReportBuffer = new StringBuffer();

        if (message.getText().equals("/help")){

            errorReportBuffer.append("您的指令有：");

            for (UserCommand userCommand: userCommands){
                //查询指令
                errorReportBuffer.append("\n -- ").append(userCommand.getCommand());
            }

        } else if (userCommands.stream().anyMatch(command -> command.getCommand().equals(message.getText()))) {

            String commandContent = userCommands.stream().
                    filter(command -> command.getCommand().equals(message.getText())).
                    findFirst().
                    map(UserCommand::getCommandContent)
                    .orElse(null);

            errorReportBuffer.append(commandContent).append("Api");//这里可以加上Api，来实现

        }else {
            errorReportBuffer.append("您输入的指令错误，请输入正确的指令");
        }

        sendMessage(String.valueOf(message.getChatId()),errorReportBuffer.toString());

    }


    private void handleGroupCommand(Message message,List<UserCommand> userCommands){

        StringBuffer errorReportBuffer = new StringBuffer();

        if (message.getText().equals("/help@"+getMapperUser().getUsername())){

            errorReportBuffer.append("您的指令有：");

            for (UserCommand userCommand: userCommands){
                //查询指令
                errorReportBuffer.append("\n -- ").append(userCommand.getCommand());
            }

        } else if (userCommands.
                stream()
                .anyMatch(command -> message.getText().equals(command.getCommand()+"@"+getMapperUser().getUsername()))) {

            String commandContent = userCommands.stream().
                    filter(command -> message.getText().equals(command.getCommand()+"@"+getMapperUser().getUsername())).
                    findFirst().
                    map(UserCommand::getCommandContent).
                    orElse(null);
            errorReportBuffer.append(commandContent).append("Api");//这里可以加上Api，来实现

        }else {
            errorReportBuffer.append("您输入的指令错误，请输入正确的指令");
        }

        sendMessage(String.valueOf(message.getChatId()),errorReportBuffer.toString());

    }

    private synchronized void sendMessage(String chatId, String text) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
