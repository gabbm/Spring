package com.tutorial.spring.dependencyinjection.mail;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

	/*
	 * Constructor annotation is prefered before property injection
	 */
	
	/*
	 * Call a resource component
	 * 
	 * @Resource
	 * private MailSender mailSender;
	 */

	/*
	 * Optional way to call a resource component
	 * 
	 * @Resource
	 * public void setMailSender(MailSender mailSender){
	 *    this.mailSender = mailSender;
	 * }
	 */

	/*
	 * Optional way to call an autowired component
	 * 
	 * @Autowired
	 * public MailController(@Qualifier("smtp") MailSender mailSender){
	 *    this.mailSender = mailSender;
	 * }
	 */
	
	/*
	 * If we don't use the name attribute, the resource will look by Type instead of Name
	 */
	
	private MailSender mailSender;
	
	@Autowired
	 public MailController(@Qualifier("smtp") MailSender mailSender){
		this.mailSender = mailSender;
	 }
	
	@RequestMapping("/mail")
	public String sendMail() throws MessagingException{
		mailSender.send("abc@example.com", "Some subject", "the content");
		
		return "Mail sent";
	}
}
