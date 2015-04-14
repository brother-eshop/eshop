package com.eshop.web.controllers.websocket;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

public class UserSessionCache {
    public static ConcurrentHashMap<String, ClientInfo> merchantSession = new ConcurrentHashMap<String, ClientInfo>();

    public static void setMerchantSession(String userName, WebSocketSession webSocketSession) {
        // 从Map中获取对应的终端信息
        ClientInfo client = merchantSession.get(userName);
        // 如果Map中不存在，则查询数据库验证终端是否有效
        if (client == null) {
            client = new ClientInfo();
            client.setSocketAdd(webSocketSession.getRemoteAddress());
            client.setUserName(userName);
            client.setWebSocketSession(webSocketSession);
            client.setLastPackageTime(System.currentTimeMillis());
            merchantSession.put(userName, client);
        } else {
            // 如果Map中存在，判断终端地址是否发生改变
            if (!(client.getSocketAdd().getHostName()
                    .equals(webSocketSession.getRemoteAddress().getHostName()))
                    ||!(client.getSocketAdd().getPort() == webSocketSession.getRemoteAddress()
                            .getPort())) {
                client.setWebSocketSession(webSocketSession);
                client.setSocketAdd(webSocketSession.getRemoteAddress());
                
            }
            client.setLastPackageTime(System.currentTimeMillis());
            merchantSession.put(userName, client);
        }
        
    }
}
