//package com.mom_management.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import com.mom_management.model.Message1;
//import com.mom_management.service.ChatService;
//
//@Controller
//public class ChatController {
//
//	@Autowired
//	private SimpMessagingTemplate simpMessagingTemplate;
//
//	@Autowired
//	private ChatService chatService;
//
//	@MessageMapping("/message")
//	@SendTo("/chatroom/public")
//	public Message1 receiveMessage(@Payload Message1 message) {
//		System.out.println("abc" + message);
//		chatService.saveMessage(message);
//		return message;
//	}
//
////    @MessageMapping("/private-message")
////    public Message1 recMessage(@Payload Message1 message){
////        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
////        System.out.println("lllll"+message.toString()+"+lll");
////        return message;
////    }
//
//	@MessageMapping("/private-message")
//	public Message1 recMessage(@Payload Message1 message) {
//		// Extracting username from receiver UserDao object
//		String receiverUsername = message.getReceiver().getUsername();
//		simpMessagingTemplate.convertAndSendToUser(receiverUsername, "/private", message);
//		System.out.println(message.toString());
//		return message;
//	}
//}

package com.mom_management.controller;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mom_management.ennum.Status;
import com.mom_management.model.GroupChat;
import com.mom_management.model.Message2;
import com.mom_management.service.ChatService;
import com.mom_management.service.GroupService;


@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
	private ChatService chatService;

    @Autowired
    private GroupService service;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message2 receiveMessage(@Payload Message2 message){
    	System.out.println("iiiiiiiiiii");
    	System.out.println("abc"+message);
     	//UserDao rece=message.getReceiverName();
    	//String rec=rece.getFirstName();
    	//chatService.saveMessage(message);
    	
    	GroupChat chat=new GroupChat();
    	List<Long> members =new ArrayList<>();
    	Long number = Long.parseLong(message.getSenderName());
    	chat.setSenderId(number);
    	Status status=null;
    	if(message.getGroupName()!=null) {
    		List<GroupChat> groupChats=service.findByName(message.getGroupName());
    		System.out.println(groupChats.toString());
    		 if (!groupChats.isEmpty()) {
    			 
    			 for (GroupChat groupChat : groupChats) {
    			        List<Long> chatMembers = groupChat.getMembers();
    			        if (!chatMembers.isEmpty()) {
    			            members = chatMembers;
    			            break; // Exit loop if non-empty members list is found
    			        }
    			    }

    			    
    			  
    	            //System.out.println("member"+groupChats.toString());
    	            //System.out.println("member"+members);
    	      }
    		
    		chat.setDate(LocalDateTime.now()); // Update the timestamp
    		
            
    	}
    	chat.setName(message.getGroupName());
    	chat.setMembers(members);
    	chat.setMessage(message.getMessage());
    	chat.setStatus(status.MESSAGE);
    	System.out.println(chat);
    	service.save(chat);
    	//simpMessagingTemplate.convertAndSendToUser(rec,"/chatroom/public",message);
        return message;
    }


    @MessageMapping("/private-message")
    public Message2 recMessage(@Payload Message2 message){
//    	UserDao rece=message.getReceiverName();
    	//String rec=rece.getFirstName();
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println("lllll"+message.toString()+"+lll");
        chatService.saveMessage(message);
        return message;
    }
    
    
    @GetMapping("/user/{senderId}/receiver/{receiverId}")
    public ResponseEntity<List<Message2>> getMessagesBetweenUsers(@PathVariable Long senderId,
                                                                 @PathVariable Long receiverId)
            throws IOException {

        List<Message2> messages = chatService.getMessagesBetweenUsers(senderId, receiverId);

        // Return the list of messages in the response entity
        return ResponseEntity.ok(messages);
    }

    
//    @GetMapping("/user/{senderId}/receiver/{receiverId}")
//    public ResponseEntity<List<Message1>> getMessagesBetweenUsers(@PathVariable Long senderId,
//                                                                 @PathVariable Long receiverId) {
//        UserDao sender = new UserDao();
//        sender.setUserId(senderId); // Assuming senderId is the ID of the sender user
//
//        UserDao receiver = new UserDao();
//        receiver.setUserId(receiverId); // Assuming receiverId is the ID of the receiver user
//        System.out.println(senderId+" "+receiverId);
//        List<Message1> messages = chatService.getMessagesBetweenUsers(sender, receiver);
//        return ResponseEntity.ok(messages);
//    }
}

