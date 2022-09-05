package com.alphabetas.chat.controller;

import com.alphabetas.chat.model.Message;
import com.alphabetas.chat.repo.ChatRepo;
import com.alphabetas.chat.repo.MessagesRepo;
import com.alphabetas.chat.repo.UserChatRepo;
import com.alphabetas.chat.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@RequestMapping("/chat")
public class MessagesController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    MessagesRepo messagesRepo;

    @Autowired
    UserChatRepo connectorRepo;

    @Autowired
    ChatRepo chatRepo;

    @GetMapping("/{chat}")
    public ModelAndView chat_id(@PathVariable String chat){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ModelAndView modelAndView = new ModelAndView("redirect:/chats");
        if(connectorRepo.findByClientAndChat(username, chat) == null){
            Set<String> error_msgs = new HashSet<>();
            modelAndView.addObject("errors", true);
            modelAndView.addObject("error_msgs", error_msgs);
            error_msgs.add("Код чату недійсний, або ви не ввійшли в чат");
            return modelAndView;
        }
        List<Message> list = messagesRepo.findAllByChat(chat);
        String invite = chatRepo.findByCode(chat).getChatInvite();
        modelAndView.addObject("invite", invite);
        modelAndView.addObject("list", list);
        modelAndView.addObject("user", username);
        modelAndView.addObject("chat", chat);
        modelAndView.addObject("members", chatRepo.findByCode(chat).getMembers());
        modelAndView.addObject("name", connectorRepo.findByClientAndChat(username, chat).getChatName());
        modelAndView.setViewName("chat");
        return modelAndView;
    }

    @PostMapping("/{chat}")
    public String newMessage(String message, @PathVariable String chat){
        Logger logger = Logger.getLogger("MsgController.PostMapping(chat)");
        logger.info("chat = " + chat);
        if(message.trim().length() != 0){
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesRepo.saveAndFlush(new Message(chat, message, username));
        }
        return ("redirect:/chat/" + chat);
    }
}
