package com.alphabetas.chat.repo;

import com.alphabetas.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {
   Chat findByCode(String code);
   Chat findByChatInvite(String chat_invite);
   void deleteById(Long id);
}
