package com.tutorial.spring.dependencyinjection;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
public class DiSpringTutorialApplication {

	private static final Log log = LogFactory.getLog(DiSpringTutorialApplication.class);
	
	public static void main(String[] args) {
		/*
		 * Configures application and return the context of the application (Beans, etc.)
		 */
		ApplicationContext applicationContext = SpringApplication.run(DiSpringTutorialApplication.class, args);
		
		/*
		 * Load application context to list Application Beans
		 */
		log.info("Beans in application context:");
		
		String beanNames[] = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		
		for(String beanName : beanNames){
			log.info(beanName);
		}
	}
}
