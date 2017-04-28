package com.tutorial.spring.dependencyinjection.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

/*
 * When we have multiple components, use this first
 * @Primary 
 */

/*
 * Provide a resource. 
 * Default name is the class name.
 * If the name of the component is the same than the resource variable, the name is not needed
 * @Component
 * @Component("name")
 */

/*
 * Identify an autowired bean with a name.
 * Default name is the class name.
 * @Qualifier("name") 
 */

/*
 * We can define it as a Bean in a Configuration Class (MailConfig.java)
 */

public class SmtpMailSender implements MailSender {
	
	private static final Log log = LogFactory.getLog(SmtpMailSender.class);
	
	/*
	 * If we want to use the @Autowired is necessary that the class will be a @Component, etc.
	 */
	
	private JavaMailSender javaMailSender;
	
	public void setJavaMailSender(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}
	
	@Override
	@Async
	public void send(String to, String subject, String body) throws MessagingException{
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		log.info("Sending SMTP mail from thread " + Thread.currentThread().getName());
		
		MimeMessageHelper mimeMessageHelper;
		
		mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setText(body, true);
		
		javaMailSender.send(mimeMessage);
	}

}
