package com.mom_management.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mom_management.model.Message1;
import com.mom_management.model.Message2;
import com.mom_management.model.UserDao;
import com.mom_management.repository.ChatRepository;
import com.mom_management.repository.UserRepository;

@Service
public class ChatService {
	
    @Autowired
    private ChatRepository messageRepository;
    

    @Autowired
    private UserRepository userRepository;

    public Message2 saveMessage(Message2 message) {
        // Perform any necessary processing or validation
        // Save the message to the database
        return messageRepository.save(message);
    }
    
    public List<Message2> getMessagesBetweenUsers(Long sender, Long receiver) {
        // Retrieve messages from both query directions
    	String senderName = String.valueOf(sender);
    	String receiverName = String.valueOf(receiver);
        List<Message2> messages1 = messageRepository.findBySenderNameAndReceiverName(senderName, receiverName);
        List<Message2> messages2 = messageRepository.findBySenderNameAndReceiverName(receiverName, senderName);

        // Merge messages from both directions into a new list (history)
        List<Message2> history = new ArrayList<>();
        history.addAll(messages1);
        history.addAll(messages2);
        Collections.sort(history, Comparator.comparing(Message2::getDate));
       // Collections.sort(history, Comparator.comparing(Message::getTimestamp).reversed());

        return history;
    }
    
//
//    @Autowired
//    private ChatRepository messageRepository;
//    
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public Message1 saveMessage(Message1 message) {
//        // Perform any necessary processing or validation
//        // Save the message to the database
//        return messageRepository.save(message);
//    }
//    
////    public Message1 sendMessage(Message1 message) {
////        try {
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            System.out.println("Authentication: " + authentication);
////            
////            if (authentication == null || !authentication.isAuthenticated()) {
////                throw new IllegalStateException("User is not authenticated");
////            }
////            
////            String currentPrincipalName = authentication.getName();
////            System.out.println("Current Principal Name: " + currentPrincipalName);
////            
////            UserDao sender = userRepository.findByEmail(currentPrincipalName);
////            if (sender == null) {
////                throw new IllegalStateException("Authenticated user not found for username: " + currentPrincipalName);
////            }
////    		//Message1 chatEntity = new Message1();
////    		message.setSenderName(userRepository.findById(sender.getUserId()).orElse(null));
////    		//chatEntity.setReceiver(userRepository.findById(message.getReceiverName()).orElse(null));
////
////    		//chatEntity.setContent(message.getContent());
////    		message.setTimestamp(LocalDateTime.now());
////    		System.out.println("data "+message);
////            
////            return messageRepository.save(message);
////        } catch (Exception e) {
////            System.err.println("Failed to send message: " + e.getMessage());
////            e.printStackTrace();
////            throw new RuntimeException("Failed to send message", e);
////        }
////    }
//    
}
//
