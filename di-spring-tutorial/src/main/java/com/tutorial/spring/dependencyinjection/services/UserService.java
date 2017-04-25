package com.tutorial.spring.dependencyinjection.services;

import com.tutorial.spring.dependencyinjection.dto.SignupForm;

public interface UserService {
	
	public abstract void signup(SignupForm signupForm);
}
