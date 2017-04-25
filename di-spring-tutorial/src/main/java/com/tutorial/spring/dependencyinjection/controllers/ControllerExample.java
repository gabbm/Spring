package com.tutorial.spring.dependencyinjection.controllers;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tutorial.spring.dependencyinjection.dto.SignupForm;
import com.tutorial.spring.dependencyinjection.services.UserService;
import com.tutorial.spring.dependencyinjection.util.Utilities;
import com.tutorial.spring.dependencyinjection.validators.SignupFormValidator;

/*
 * Controller writes in the response, redirecting to a view
 */
@Controller
public class ControllerExample {

	private static final Log log = LogFactory.getLog(ControllerExample.class);
	
	@Value("${app.name}")
	private String appName;
	
	private UserService userService;
	private SignupFormValidator signupFormValidator;
	
	@Autowired
	public ControllerExample(UserService userService, SignupFormValidator signupFormValidator){
		this.userService = userService;
		this.signupFormValidator = signupFormValidator;
	}
	
	/*
	 * Use custom validator for model attributes "signupForm"
	 */
	@InitBinder("signupForm")
	protected void initSignupBinder(WebDataBinder binder){
		binder.setValidator(signupFormValidator);
	}
	
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
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm,
			BindingResult result, RedirectAttributes redirectAttributes){
		
		if(result.hasErrors()){
			return "signup";
		}
		
		userService.signup(signupForm);
		
		/*
		 * Temporary messages sent to the view
		 */
		Utilities.flash(redirectAttributes, "success",
				"signupSuccess");
		
		/*
		 * Redirect current view to home url (/) 
		 */
		return "redirect:/";
	}
}
