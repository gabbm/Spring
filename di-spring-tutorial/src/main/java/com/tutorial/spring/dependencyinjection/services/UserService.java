package com.tutorial.spring.dependencyinjection.services;

import org.springframework.validation.BindingResult;

import com.tutorial.spring.dependencyinjection.dto.ForgotPasswordForm;
import com.tutorial.spring.dependencyinjection.dto.ResetPasswordForm;
import com.tutorial.spring.dependencyinjection.dto.SignupForm;
import com.tutorial.spring.dependencyinjection.dto.UserEditForm;
import com.tutorial.spring.dependencyinjection.entities.User;

public interface UserService {
	
	public abstract void signup(SignupForm signupForm);
	
	public abstract void verify(String verificationCode);
	
	public abstract void forgotPassword(ForgotPasswordForm forgotPasswordForm);
	
	public abstract void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm,
			BindingResult result);
	
	public abstract User findOne(long userId);

	public abstract void update(long userId, UserEditForm userEditForm);
}
