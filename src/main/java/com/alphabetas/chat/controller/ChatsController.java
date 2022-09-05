package com.alphabetas.chat.controller;

import com.alphabetas.chat.model.Chat;
import com.alphabetas.chat.model.ClientChat;
import com.alphabetas.chat.repo.ChatRepo;
import com.alphabetas.chat.repo.UserChatRepo;
import com.alphabetas.chat.service.utils.CreateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/chats")
public class ChatsController {
    @Autowired
    ChatRepo chatRepo;

    @Autowired
    UserChatRepo userChatRepo;

    @GetMapping("")
    public ModelAndView chats() {
        ModelAndView modelAndView = new ModelAndView("chats");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ClientChat> clientChatList = userChatRepo.findAllByClient(username);
        modelAndView.addObject("user", username);
        modelAndView.addObject("chats", clientChatList);
        modelAndView.addObject("code", CreateCodeUtil.createInviteCode());
        modelAndView.addObject("all_chats", chatRepo.findAll());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createChat(String chatName, String chatInviteCode_hidden) {
        ModelAndView modelAndView = new ModelAndView("redirect:/chats");
        Set<String> error_msgs = new HashSet<>();
        modelAndView.addObject("error_msgs", error_msgs);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String chatCode = CreateCodeUtil.createCode(10);
        if (chatName.trim().length() == 0) {
            modelAndView.addObject("errors", true);
            modelAndView.addObject("code", CreateCodeUtil.createInviteCode());
            error_msgs.add("Назва чату не може бути пустою!");
            return modelAndView;
        }
        if (chatRepo.findByCode(chatCode) == null &&
                chatRepo.findByChatInvite(chatInviteCode_hidden) == null) {
            chatRepo.save(new Chat(chatCode, chatName, chatInviteCode_hidden, 1));
            if (userChatRepo.findByClientAndChat(username, chatCode) == null)
                userChatRepo.save(new ClientChat(chatCode, username, chatName, chatInviteCode_hidden));
        }
        return modelAndView;
    }

    @PostMapping("/join")
    public ModelAndView joinChat(String invite) {
        invite = invite.trim();
        ModelAndView modelAndView = new ModelAndView("redirect:/chats");
        if (invite.length() < 6) {
            Set<String> error_msgs = new HashSet<>();
            modelAndView.addObject("errors", true);
            modelAndView.addObject("error_msgs", error_msgs);
            error_msgs.add("Довжина запрошення чату не може бути меншою за 6 символів");
            return modelAndView;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = invite.length() - 6; i < invite.length(); i++) {
            builder.append(invite.charAt(i));
        }

        modelAndView.clear();
        modelAndView.setViewName("redirect:/invite/" + builder);
        return modelAndView;
    }
}
