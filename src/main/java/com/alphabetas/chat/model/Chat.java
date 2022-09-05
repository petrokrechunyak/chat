package com.alphabetas.chat.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String code;
    @NotBlank
    private String chatName;
    @NotBlank
    private String chatInvite;
    @Min(1)
    private Integer members;

    public Chat(String code, String chatName, String chatInvite, Integer members) {
        this.code = code;
        this.chatName = chatName;
        this.chatInvite = chatInvite;
        this.members = members;
    }
}