package com.alphabetas.chat.controller.websocket;

import com.alphabetas.chat.model.Message;
import com.alphabetas.chat.repo.MessagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
public class WebSocketController {

    @Autowired
    MessagesRepo messagesRepo;

    @MessageMapping("/send_msg")
    @SendTo("/current_chat/chat")
    public Message get_and_send(Message msg){
        Message new_msg = new Message(msg.getChat(), msg.getMessage(), msg.getAuthor());
        messagesRepo.save(new_msg);
        return new_msg;
    }
}
