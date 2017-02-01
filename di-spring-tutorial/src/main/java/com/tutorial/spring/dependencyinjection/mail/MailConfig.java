package com.tutorial.spring.dependencyinjection.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.tutorial.spring.dependencyinjection.bean.DemoBean;

/*
 * You can configure Beans using @Configuration or @Component
 * 
 * If we use @Configuration the values of the @Beans are cached!
 * If we use @Component the @Beans could be created X times!
 */

@Configuration
public class MailConfig {
	
	/*
	 * Define components as a bean
	 * @Bean
	 */
	
	/*
	 * Establish which Beans are active by profile
	 * @Profile("name") 
	 * !name <- Any profile without this name
	 */
	
	/*
	 * Add conditionals to execute a Bean if something happens
	 * @Conditional
	 * @ConditionalOnClass
	 * @ConditionalOnProperty
	 * @Conditional...
	 */
	
	/*
	 * If we use parameters spring will inject them automatically.
	 * We can access another defined Beans.
	 */
	
	/*
	 * We can use another Beans calling the methods defined in this current configuration class
	 * If we call this multiple times, it only will create one the first time and the it will use the same
	 */

	@Bean
	public DemoBean demoBean(){
		return new DemoBean();
	}
	
	@Bean
	@ConditionalOnProperty(name="spring.mail.host",
			havingValue = "foo",
			matchIfMissing = true)
	public MailSender mockMailSender(){
		return new MockMailSender();
	}
	
	@Bean
	@ConditionalOnProperty(name="spring.mail.host")
	public MailSender smtpMailSender(JavaMailSender javaMailSender){
		SmtpMailSender smtpMailSender = new SmtpMailSender();
		smtpMailSender.setJavaMailSender(javaMailSender);
		
		demoBean().foo();
		
		return smtpMailSender;
	}
}
