package com.tutorial.spring.dependencyinjection.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	/*
	 * Get property of application.properties file by code
	 * @Value("${code}")
	 */
	
	@Value("${app.name}")
	private String appName;
	
	@RequestMapping("/hello")
	public String hello(){
		return "Hello " + appName;
	}
}
