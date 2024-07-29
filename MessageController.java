package com.mom_management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mom_management.config.ChatWebSocketComponent;
import com.mom_management.dto.MessageDTO;
import com.mom_management.model.Message1;
import com.mom_management.model.UserDao;
import com.mom_management.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ChatWebSocketComponent chatWebSocketComponent;

	@Autowired
	private ObjectMapper mapper;

//	    @GetMapping("/{senderId}")
//	    public List<Message> getMessagesBySenderId(@PathVariable Long senderId) {
//	        return messageService.getMessagesBySenderId1(senderId);
//	    }

	@PostMapping
	public Message1 sendMessage(@RequestBody MessageDTO message) throws IOException {

		Message1 message2 = messageService.sendMessage(message);

		String changeMessage = mapper.writeValueAsString(message2);

		chatWebSocketComponent.sendMessageToAllSessions(changeMessage);
		 // Print the message to the console
	    System.out.println("Message sent: " + changeMessage);

		return message2;

	}

	@GetMapping("/user/{senderId}/receiver/{receiverId}")
	public ResponseEntity<String> getMessagesBetweenUsers(@PathVariable Long senderId, @PathVariable Long receiverId)
			throws IOException {
		UserDao sender = new UserDao();
		sender.setUserId(senderId); // Assuming senderId is the ID of the sender user

		UserDao receiver = new UserDao();
		receiver.setUserId(receiverId); // Assuming receiverId is the ID of the receiver user

		List<Message1> messages = messageService.getMessagesBetweenUsers(sender, receiver);

		// Convert the list of messages to a string
		String messagesAsString = convertMessagesToString(messages);

		// Call the sendMessageToAllSessions method with the messages as a string
		chatWebSocketComponent.sendMessageToAllSessions(messagesAsString);
		
		// Return the messages as a string in the response entity
		return ResponseEntity.ok(messagesAsString);
	}

	// Method to convert list of messages to string
	private String convertMessagesToString(List<Message1> messages) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Message1 message : messages) {
			// Access the sender's and receiver's IDs through their UserDao objects
			Long messageId = message.getId();
			Long senderId = message.getSender().getUserId();
			Long receiverId = message.getReceiver().getUserId();

			// Append senderId and receiverId along with content and timestamp to the string
			// builder
			stringBuilder.append("MessageId: ").append(messageId).append("\n");
			stringBuilder.append("SenderId: ").append(senderId).append("\n");
			stringBuilder.append("ReceiverId: ").append(receiverId).append("\n");
			stringBuilder.append("Content: ").append(message.getContent()).append("\n");
			stringBuilder.append("Timestamp: ").append(message.getTimestamp()).append("\n\n");
		}
		// Convert the string builder to a string and return
		return stringBuilder.toString();
	}

}
