package com.tutorial.spring.dependencyinjection.validators;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.tutorial.spring.dependencyinjection.dto.SignupForm;
import com.tutorial.spring.dependencyinjection.entities.User;
import com.tutorial.spring.dependencyinjection.repositories.UserRepository;

@Component
public class SignupFormValidator extends LocalValidatorFactoryBean{
	
	private UserRepository userRepository;
	
	@Resource
	public void setUserRepository(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(SignupForm.class);
	}
	
	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints) {
		super.validate(obj, errors, validationHints);
		
		if(!errors.hasErrors()){
			SignupForm signupForm = (SignupForm) obj;
			User user = userRepository.findByEmail(signupForm.getEmail());
			
			if(user != null){
				errors.rejectValue("email", "emailNotUnique");
			}
		}
	}
}
