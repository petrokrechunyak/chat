package com.alphabetas.chat.controller;

import com.alphabetas.chat.model.Chat;
import com.alphabetas.chat.model.ClientChat;
import com.alphabetas.chat.repo.ChatRepo;
import com.alphabetas.chat.repo.UserChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/invite")
public class InviteController {
    @Autowired
    UserChatRepo userChatRepo;

    @Autowired
    ChatRepo chatRepo;

    @GetMapping("/{invite}")
    public ModelAndView invite(@PathVariable String invite){
        ModelAndView modelAndView = new ModelAndView("redirect:/chats");
        Chat chat = chatRepo.findByChatInvite("localhost:8090/invite/" + invite);
        if(chat == null) {
            Set<String> error_msgs = new HashSet<>();
            modelAndView.addObject("errors", true);
            modelAndView.addObject("error_msgs", error_msgs);
            error_msgs.add("Такого запрошення не існує!");
            return modelAndView;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userChatRepo.findByClientAndChat(username, chat.getCode()) != null)
            return modelAndView;
        userChatRepo.save(new ClientChat(chat.getCode(), username, chat.getChatName(), chat.getChatInvite()));

        chat.setMembers(chat.getMembers()+1);
        chatRepo.deleteById(chat.getId());
        chatRepo.save(chat);

        modelAndView.setViewName("redirect:/chat/" + chat.getCode());
        return modelAndView;
    }
}
