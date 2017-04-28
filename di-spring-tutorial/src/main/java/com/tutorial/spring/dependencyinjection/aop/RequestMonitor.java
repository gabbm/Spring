package com.tutorial.spring.dependencyinjection.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RequestMonitor {
	
	private static final Log log = LogFactory.getLog(RequestMonitor.class);
	
	/*
	 * Advice method is called before or after or surrounding a business functionality
	 * @Around -> surrounding
	 * We should put the Point Cut where it will be used
	 */
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object wrap(ProceedingJoinPoint pjp) throws Throwable{
		log.info("Before controller method " + pjp.getSignature().getName() + ". Thread " + Thread.currentThread().getName());
		Object retVal = pjp.proceed();
		log.info("Controller method " + pjp.getSignature().getName() + " execution successful");
		
		return retVal;
	}
}
