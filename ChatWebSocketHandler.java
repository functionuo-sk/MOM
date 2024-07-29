package com.mom_management.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ChatWebSocketHandler implements WebSocketHandler{

	 
	    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	        // Handle incoming messages
	    }
	    
	    @Override
	    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	        // Handle transport errors
	    }

	    @Override
	    public boolean supportsPartialMessages() {
	        return false;
	    }

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			// TODO Auto-generated method stub
			
		}
	 
}
