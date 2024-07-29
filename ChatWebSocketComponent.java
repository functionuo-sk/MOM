package com.mom_management.config;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketComponent extends TextWebSocketHandler{
	
	 private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	 
	 @Override
	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	        // Add the new session to the list
	        sessions.add(session);
	    }

	    @Override
	    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
	        // Remove the closed session from the list
	        sessions.remove(session);
	    }

	    @Override
	    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
	        // Handle incoming text messages
	        String payload = message.getPayload();
	        // You can process the incoming message here
	    }
	    
	    public static String sendMessageToAllSessions(String message) throws IOException {
	        TextMessage textMessage = new TextMessage(message);
	        for (WebSocketSession session : sessions) {
	            session.sendMessage(textMessage);
	        }
	        
	        return "success";
	    }

}
