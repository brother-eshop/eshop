package com.eshop.frameworks.core.mail;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eshop.common.util.io.PropertyUtil;
import com.eshop.frameworks.core.entity.EmailParam;

public class MailSenderService {
	private static PropertyUtil propertyUtil = PropertyUtil.getInstance("project");
	/**注入线程池*/
    private EmailExecutorPool emailExecutorPool;  
	private JavaMailSender mailSender;//spring配置中定义
	private VelocityEngine velocityEngine;//spring配置中定义
	private SimpleMailMessage simpleMailMessage;//spring配置中定义
	private String from;
	private String to;
	private String[] exceptionTo = getToEmails();
	private String exceptionSubject = "异常告警";
	private String subject;
	private String content;
	private String templateName;
	// 是否需要身份验证   
	private boolean validate = false; 
	public void setParam(EmailParam ep) {
		this.to = ep.getToEmail();
		this.subject = ep.getSubject();
		this.templateName = ep.getTemplateName();
	}
	
	private String[] getToEmails() {
		String tos = propertyUtil.getProperty("toEmails");
		String [] arrTos = tos.split(",");
		return arrTos;
	}

	public EmailExecutorPool getEmailExecutorPool() {
		return emailExecutorPool;
	}

	public void setEmailExecutorPool(EmailExecutorPool emailExecutorPool) {
		this.emailExecutorPool = emailExecutorPool;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	/**
	 * 发送模板邮件
	 *
	 */
	public void sendWithTemplate(Map model){
		mailSender = this.getMailSender();
		simpleMailMessage.setTo(this.getTo()); //接收人  
		simpleMailMessage.setFrom(simpleMailMessage.getFrom()); //发送人,从配置文件中取得
		simpleMailMessage.setSubject(this.getSubject());
        String result = null;
        try {
        	result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), this.getTemplateName(), "UTF-8",model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		simpleMailMessage.setText(result);
        mailSender.send(simpleMailMessage);
        System.out.println();
	}
	/**
	 * 发送模板邮件
	 * @throws MessagingException 
	 *
	 */
	public void sendWithTemplate(EmailParam ep,Map model) throws MessagingException{
		this.setParam(ep);
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
		try {
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			String result = VelocityEngineUtils.mergeTemplateIntoString(this.getVelocityEngine(), this.getTemplateName(), "UTF-8",model);
			messageHelper.setText(result,true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    mailSender.send(mimeMessage);
//		ExecutorService emailservice = emailExecutorPool.getService();	// 测试注入OK 
//		ExecutorService emailservice = Executors.newFixedThreadPool(1);	// 测试指定一个OK
//		EmailSendTask et = new EmailSendTask(mimeMessage,mailSender);
//		emailservice.execute(et);
		emailExecutorPool.getService().execute(new EmailSendTask(mimeMessage,mailSender));
	}
	/**
	 * 发送异常邮件
	 *
	 */
	public void sendException(String msg){
		mailSender = this.getMailSender();
		simpleMailMessage.setTo(this.getExceptionTo()); //接收人  
		simpleMailMessage.setFrom(simpleMailMessage.getFrom()); //发送人,从配置文件中取得
		simpleMailMessage.setSubject(this.getExceptionSubject());
		simpleMailMessage.setText(msg==null?"信息为空，请检查":msg);
//	    mailSender.send(simpleMailMessage);
		emailExecutorPool.getService().execute(new EmailSendTask(simpleMailMessage,mailSender));
	}
	/**
	 * 发送普通文本邮件
	 *
	 */
	public void sendException(String msg,String method){
		mailSender = this.getMailSender();
		simpleMailMessage.setTo(this.getExceptionTo()); //接收人  
		simpleMailMessage.setFrom(simpleMailMessage.getFrom()); //发送人,从配置文件中取得
		simpleMailMessage.setSubject(this.getExceptionSubject() + method);
		simpleMailMessage.setText(msg==null?"信息为空，请检查":msg);
//	    mailSender.send(simpleMailMessage);
		emailExecutorPool.getService().execute(new EmailSendTask(simpleMailMessage,mailSender));
	}
	/**
	 * 发送普通文本邮件
	 *
	 */
	public void sendText(){
		mailSender = this.getMailSender();
		simpleMailMessage.setTo(this.getTo()); //接收人  
		simpleMailMessage.setFrom(simpleMailMessage.getFrom()); //发送人,从配置文件中取得
		simpleMailMessage.setSubject(this.getSubject());
		simpleMailMessage.setText(this.getContent());
//	    mailSender.send(simpleMailMessage);
	    emailExecutorPool.getService().execute(new EmailSendTask(simpleMailMessage,mailSender));
	}
	/**
	 * 发送普通Html邮件
	 *
	 */
	public void sendHtml(){
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		try {
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			messageHelper.setText(this.getContent(),true);      
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    mailSender.send(mimeMessage);
	    emailExecutorPool.getService().execute(new EmailSendTask(mimeMessage,mailSender));
	}
	/**
	 * 发送普通带一张图片的Html邮件
	 *
	 */
	public void sendHtmlWithImage(String imagePath){
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			messageHelper.setText(this.getContent(),true);
//			Content="<html><head></head><body><img src=\"cid:image\"/></body></html>";
			//图片必须这样子：<img src='cid:image'/>
			FileSystemResource img = new FileSystemResource(new File(imagePath));
			messageHelper.addInline("image",img);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    mailSender.send(mimeMessage);
	    emailExecutorPool.getService().execute(new EmailSendTask(mimeMessage,mailSender));
	}
	/**
	 * 发送普通带附件的Html邮件
	 *
	 */
	public void sendHtmlWithAttachment(String filePath){
		mailSender = this.getMailSender();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
			messageHelper.setTo(this.getTo());
			messageHelper.setFrom(simpleMailMessage.getFrom());
			messageHelper.setSubject(this.getSubject());
			messageHelper.setText(this.getContent(),true);
			FileSystemResource file = new FileSystemResource(new File(filePath));
//			System.out.println("file.getFilename==="+file.getFilename());
			messageHelper.addAttachment(file.getFilename(),file);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    mailSender.send(mimeMessage);
	    emailExecutorPool.getService().execute(new EmailSendTask(mimeMessage,mailSender));
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isValidate() {
		return validate;
	}

	public String[] getExceptionTo() {
		return exceptionTo;
	}

	public void setExceptionTo(String[] exceptionTo) {
		this.exceptionTo = exceptionTo;
	}

	public String getExceptionSubject() {
		return exceptionSubject;
	}

	public void setExceptionSubject(String exceptionSubject) {
		this.exceptionSubject = exceptionSubject;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}

}

