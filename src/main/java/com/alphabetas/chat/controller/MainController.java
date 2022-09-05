package com.alphabetas.chat.controller;

import com.alphabetas.chat.model.User;
import com.alphabetas.chat.repo.ChatRepo;
import com.alphabetas.chat.repo.MessagesRepo;
import com.alphabetas.chat.repo.UserChatRepo;
import com.alphabetas.chat.repo.UserRepo;
import com.alphabetas.chat.service.UserDetailsServiceImpl;
import com.alphabetas.chat.service.utils.EncodePasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    MessagesRepo messagesRepo;

    @Autowired
    ChatRepo chatRepo;

    @Autowired
    UserChatRepo userChatRepo;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping
    public String home(){
        String auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(auth.equals("[ROLE_ANONYMOUS]"))
            return "redirect:/login";
        return "redirect:/chats";
    }

    @GetMapping("/registration")
    public String register(){
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView register(String username, String password, String password_confirm){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
            Set<String> error_msgs = new HashSet<>();
            modelAndView.addObject("error_msgs", error_msgs);
        if(userRepo.findByUsername(username) != null){
            modelAndView.addObject("errors", true);
            error_msgs.add("Аккаунт з таким іменем користувача вже існує!");
            return modelAndView;
        }

        if(!password.equals(password_confirm)){
            modelAndView.addObject("errors", true);
            error_msgs.add("Паролі не співпадають!");
            return modelAndView;
        }

        try {
            userRepo.saveAndFlush(new User(username, EncodePasswordUtil.encode(password)));
        } catch (ConstraintViolationException e) {
            modelAndView.addObject("errors", true);
            for(ConstraintViolation<?> constraintViolation: e.getConstraintViolations()) {
                error_msgs.add(constraintViolation.getMessage());
            }
            return modelAndView;
        }
        authByUsername(username);
        modelAndView.clear();
        modelAndView.setViewName("redirect:/chats/");
        return modelAndView;
    }

    @GetMapping("/login")
    public String accessALl(){
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(String username, String password){
        ModelAndView modelAndView = new ModelAndView("login");
        Set<String> error_msgs = new HashSet<>();
        modelAndView.addObject("error_msgs", error_msgs);
        if(userRepo.findByUsername(username) == null){
            modelAndView.addObject("errors", true);
            error_msgs.add("Акаунту з таким іменем користувача не існує!");
            return modelAndView;
        }
        User userModel = userRepo.findByUsername(username);

        if(!EncodePasswordUtil.equal(password, userModel.getPassword())){
            modelAndView.addObject("errors", true);
            error_msgs.add("Пароль невірний!");
            return modelAndView;
        }
        authByUsername(username);

        modelAndView.clear();
        modelAndView.setViewName("redirect:/chats");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(){
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    private void authByUsername(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}