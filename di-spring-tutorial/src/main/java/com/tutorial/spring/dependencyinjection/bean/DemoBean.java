package com.tutorial.spring.dependencyinjection.bean;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class DemoBean {

	private static final Log log = LogFactory.getLog(DemoBean.class);
	
	public DemoBean(){
		log.info("DemoBean created!");
	}
	
	public String foo(){
		return "Something";
	}
}
