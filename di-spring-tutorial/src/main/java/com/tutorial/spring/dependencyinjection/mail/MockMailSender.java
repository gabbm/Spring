package com.tutorial.spring.dependencyinjection.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public class MockMailSender implements MailSender{

	private static final Log log = LogFactory.getLog(MockMailSender.class);
	
	@Override
	public void send(String to, String subject, String body){
		log.info("Sending mail to: " + to);
		log.info("Subject: " + subject);
		log.info("Body: " + body);
	}
}
