package com.eshop.web.controllers.websocket;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
public class UserLinkHandler implements WebSocketHandler {  
    /*@Override  
    protected void handleTextMessage(WebSocketSession session,  
            TextMessage message) throws Exception {  
        super.handleTextMessage(session, message); 
        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server");  
        session.sendMessage(returnMessage);  
    }*/
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        // TODO Auto-generated method stub
    }
    @Override
    public void handleMessage(WebSocketSession session,
            WebSocketMessage<?> message) throws Exception {
        // TODO Auto-generated method stub
//        Set<String> keys =attr.keySet();
//        Iterator<String> iter = keys.iterator();
//        while(iter.hasNext()){
//            System.out.println("handleMessage:"+attr.get(iter.next()));
//        }
        Object mess = message.getPayload();
        if(mess instanceof String){
            System.out.println(mess.toString());
            
            session.sendMessage(new TextMessage("heartbeat".getBytes()));
            UserSessionCache.setMerchantSession((String)mess,session);
        }
    }
    @Override
    public void handleTransportError(WebSocketSession session,
            Throwable exception) throws Exception {
        // TODO Auto-generated method stub
        ChannelPool.removeChannelByChannel(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus closeStatus) throws Exception {
        // TODO Auto-generated method stub
        ChannelPool.removeChannelByChannel(session);
    }
    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        return false;
    }
}  