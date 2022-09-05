package com.alphabetas.chat.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "clientChats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chat;
    private String client;
    private String chatName;
    private String chatInvite;

    public ClientChat(String chat, String client, String chatName, String chatInvite) {
        this.chat = chat;
        this.client = client;
        this.chatName = chatName;
        this.chatInvite = chatInvite;
    }
}
