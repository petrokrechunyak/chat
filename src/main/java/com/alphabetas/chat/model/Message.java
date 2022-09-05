package com.alphabetas.chat.model;

import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chat;
    private String message;
    private String author;
    private String date;
    @Transient
    private String currentAuthor;
    public Message(String chat, String message, String author) {
        this.chat = chat;
        this.message = message;
        this.author = author;
        this.date = getCurrentDate();
    }

    private static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date_d = new Date();
        return formatter.format(date_d);
    }
}
