package com.eshop.web.controllers.websocket;


import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;


public class ChannelPool {
	
	private static final Logger logger = Logger.getLogger(ChannelPool.class);
	
	public static ClientInfo getSessionByName(Map<String,ClientInfo> clientChannelMap ,String userName){
		Iterator<String> iterator = clientChannelMap.keySet().iterator();
		while (iterator.hasNext()) {
			String workNum = (String) iterator.next();
			if(workNum.equals(userName)){
				ClientInfo userInfo = clientChannelMap.get(workNum);
				logger.info("从当前的Map中匹配到对应的session信息,并成功返回!");
				return userInfo;
			}
		}
		return null;
	}
	
	/**
	 * 从当前的Map中移除对应的Channel信息
	 */
	public static void removeChannelByChannel(WebSocketSession session){
	    Lock lock = new ReentrantLock();
        lock.lock();
	    
		Iterator<String> iterator = UserSessionCache.merchantSession.keySet().iterator();
		while (iterator.hasNext()) {
			String workNum = (String) iterator.next();
			ClientInfo clientInfo = UserSessionCache.merchantSession.get(workNum);
			if(session != null && (session.getRemoteAddress().getHostName().equals(clientInfo.getSocketAdd().getHostName()) && session.getRemoteAddress().getPort() == clientInfo.getSocketAdd().getPort())){
			    UserSessionCache.merchantSession.remove(workNum);
				logger.info("服务器监听到终端通道关闭,从当前Map中匹配到对应的session信息,【清除成功】!");
			}
		}
		lock.unlock();
	}
}
