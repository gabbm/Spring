package com.tutorial.spring.dependencyinjection.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tutorial.spring.dependencyinjection.dto.UserEditForm;
import com.tutorial.spring.dependencyinjection.entities.User;
import com.tutorial.spring.dependencyinjection.services.UserService;
import com.tutorial.spring.dependencyinjection.util.Utilities;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@RequestMapping("/{verificationCode}/verify")
	public String verify(@PathVariable("verificationCode") String verificationCode,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		userService.verify(verificationCode);
		Utilities.flash(redirectAttributes,  "success", "verificationSuccess");
		
		/*
		 * Logout user
		 */
		request.logout();
		
		return "redirect:/";
	}
	
	@RequestMapping("/{userId}")
	public String getById(@PathVariable("userId") long userId, Model model){
		/*
		 * If we don't write the name of the attribute, it will use the name of the object class in camelCase
		 * 
		 * model.addAttribute("user", userService.findOne(userId));
		 */
		model.addAttribute(userService.findOne(userId));
		
		return "user";
	}
	
	@RequestMapping(value = "/{userId}/edit")
	public String edit(@PathVariable("userId") long userId, Model model){
		User user = userService.findOne(userId);
		UserEditForm userEditForm = new UserEditForm();
		
		userEditForm.setName(user.getName());
		userEditForm.setRoles(user.getRoles());
		model.addAttribute(userEditForm);
		
		return "user-edit";
	}
	
	@RequestMapping(value = "/{userId}/edit", method = RequestMethod.POST)
	public String edit(@PathVariable("userId") long userId, 
			@ModelAttribute("userEditForm") UserEditForm userEditForm,
			BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws ServletException{
		
		if(result.hasErrors()){
			return "user-edit";
		}
		
		userService.update(userId, userEditForm);
		Utilities.flash(redirectAttributes, "success", "editSuccessful");
		request.logout();
		
		return "redirect:/";
	}
}
