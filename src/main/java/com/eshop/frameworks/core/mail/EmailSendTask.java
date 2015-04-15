package com.eshop.frameworks.core.mail;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSendTask implements Runnable{
	
	private JavaMailSender javaMailSender;
	
	SimpleMailMessage simpleMailMessage;
	MimeMessage mimeMessage;
	
	public EmailSendTask(SimpleMailMessage simpleMailMessage,JavaMailSender javaMailSender){
		this.simpleMailMessage = simpleMailMessage;
		this.javaMailSender = javaMailSender;
	}
	public EmailSendTask(MimeMessage mimeMessage,JavaMailSender javaMailSender){
		this.mimeMessage = mimeMessage;
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void run() {
		if(this.simpleMailMessage != null){
			javaMailSender.send(this.simpleMailMessage);
		}else if(this.mimeMessage != null){
			javaMailSender.send(this.mimeMessage);
		}
	}

}