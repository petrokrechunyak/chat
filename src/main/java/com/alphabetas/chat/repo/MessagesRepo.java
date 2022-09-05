package com.alphabetas.chat.repo;

import com.alphabetas.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepo extends JpaRepository<Message, Long> {
    List<Message> findAllByChat(String chat);
}
