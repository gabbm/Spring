package com.tutorial.spring.dependencyinjection.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * Class with MVC Spring configuration
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter{
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/hello").setViewName("home");
	}
}
