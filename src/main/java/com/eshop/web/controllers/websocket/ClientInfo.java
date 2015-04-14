package com.eshop.web.controllers.websocket;

import java.net.InetSocketAddress;

import org.springframework.web.socket.WebSocketSession;

public class ClientInfo {
    /** 数据通道 */
    private WebSocketSession webSocketSession;
    /** userName */
    private String userName;
    /** 客户端Socket地址 */
    private InetSocketAddress socketAdd;
    
    private Long lastPackageTime;

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public InetSocketAddress getSocketAdd() {
        return socketAdd;
    }

    public void setSocketAdd(InetSocketAddress socketAdd) {
        this.socketAdd = socketAdd;
    }

    public Long getLastPackageTime() {
        return lastPackageTime;
    }

    public void setLastPackageTime(Long lastPackageTime) {
        this.lastPackageTime = lastPackageTime;
    }


}
