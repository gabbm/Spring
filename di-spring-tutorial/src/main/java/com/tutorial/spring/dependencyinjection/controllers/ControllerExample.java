package com.tutorial.spring.dependencyinjection.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tutorial.spring.dependencyinjection.dto.SignupForm;

/*
 * Controller writes in the response, redirecting to a view
 */
@Controller
public class ControllerExample {

	private static final Log log = LogFactory.getLog(ControllerExample.class);
	
	@Value("${app.name}")
	private String appName;
	
	
	/* 
	 * If we only want to redirect to View we should add the ViewController in MvcConfig class
	 *
		/*
		 * ResponseBody writes directly in the response body sending Strings if it's needed if you
		 * not put the @ResponseBody Spring try to use ViewResolver
		 *	
		@RequestMapping("/hello")
		@ResponseBody
		public String hello(){
			/*
			 * Spring try to use ViewResolver to find the view (defined in application.properties)
			 *
			return "home";
		}
	*/
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model){
		
		/*
		 * If we not write the name it use the classname in camelCase
		 */
		model.addAttribute(new SignupForm());
		
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") SignupForm signupForm){
		
		log.info(signupForm.toString());
		
		/*
		 * Redirect current view to home url (/) 
		 */
		return "redirect:/";
	}
}
