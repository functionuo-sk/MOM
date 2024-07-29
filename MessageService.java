package com.mom_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.mom_management.dto.MessageDTO;

import com.mom_management.model.Message1;
import com.mom_management.model.UserDao;
import com.mom_management.repository.MessageRepository;
import com.mom_management.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {

	@Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public Message1 sendMessage(MessageDTO message) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentication: " + authentication);
            
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalStateException("User is not authenticated");
            }
            
            String currentPrincipalName = authentication.getName();
            System.out.println("Current Principal Name: " + currentPrincipalName);
            
            UserDao sender = userRepository.findByEmail(currentPrincipalName);
            if (sender == null) {
                throw new IllegalStateException("Authenticated user not found for username: " + currentPrincipalName);
            }
    		Message1 chatEntity = new Message1();
    		chatEntity.setSender(userRepository.findById(sender.getUserId()).orElse(null));
    		chatEntity.setReceiver(userRepository.findById(message.getReceiverId()).orElse(null));

    		chatEntity.setContent(message.getContent());
    		chatEntity.setTimestamp(LocalDateTime.now());
    		chatEntity.setStatus(message.getStatus());    		
            
            return messageRepository.save(chatEntity);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send message", e);
        }
    }


    public List<Message1> getMessagesBetweenUsers(UserDao sender, UserDao receiver) {
        // Retrieve messages from both query directions
        List<Message1> messages1 = messageRepository.findBySenderOrReceiver(sender, receiver);
        List<Message1> messages2 = messageRepository.findBySenderOrReceiver(receiver, sender);

        // Merge messages from both directions into a new list (history)
        List<Message1> history = new ArrayList<>();
        history.addAll(messages1);
        history.addAll(messages2);
        Collections.sort(history, Comparator.comparing(Message1::getTimestamp));
       // Collections.sort(history, Comparator.comparing(Message::getTimestamp).reversed());

        return history;
    }

		
}
