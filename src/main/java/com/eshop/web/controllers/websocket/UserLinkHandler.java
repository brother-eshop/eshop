package com.eshop.web.controllers.websocket;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;  
import org.springframework.web.socket.WebSocketSession;  
import org.springframework.web.socket.handler.TextWebSocketHandler; 
public class UserLinkHandler extends TextWebSocketHandler {  
    public static ConcurrentHashMap<String, WebSocketSession> merchantSession = new ConcurrentHashMap<String, WebSocketSession>(); 
    @Override  
    protected void handleTextMessage(WebSocketSession session,  
            TextMessage message) throws Exception {  
        super.handleTextMessage(session, message); 
        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server");  
        session.sendMessage(returnMessage);  
    }
}  