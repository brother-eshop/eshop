package com.eshop.service.manager;

import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.eshop.web.controllers.websocket.ClientInfo;
import com.eshop.web.controllers.websocket.UserSessionCache;

@Service
public class UserSessionJob {
    public void removeInvalideSession() {  
        Lock lock = new ReentrantLock();
        lock.lock();
        
        long t = System.currentTimeMillis();
        String key ;
        for (Entry<String, ClientInfo> entry : UserSessionCache.merchantSession.entrySet()) {
            ClientInfo bean = (ClientInfo) UserSessionCache.merchantSession.get(entry.getKey());
            if (t - bean.getLastPackageTime()> 10000l) {
                key = entry.getKey();
                UserSessionCache.merchantSession.remove(key);
                System.out.println("remove session:"+key);
                continue;
            }
        }
        lock.unlock();
        System.out.println("job is running");
    }  
}
