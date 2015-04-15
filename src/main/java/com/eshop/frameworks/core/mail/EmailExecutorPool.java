package com.eshop.frameworks.core.mail;
import org.springframework.beans.factory.InitializingBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailExecutorPool {
	/**线程池数量,spring配置文件中配置*/
	private Integer poolSize;
	
	private ExecutorService service;
	public EmailExecutorPool(){
	}

	
	public ExecutorService getService(Integer number) {
		service = Executors.newFixedThreadPool(number); 
		return service;
	}
	
	public ExecutorService getService() {
		if(null == service){
			service = Executors.newFixedThreadPool(poolSize);
			return service;			
		}
		return service;
	}

	public void setService(ExecutorService service) {
	}

	public Integer getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
	}

}
