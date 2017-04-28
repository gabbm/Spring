package com.tutorial.spring.dependencyinjection.services.impl;

import javax.mail.MessagingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;

import com.tutorial.spring.dependencyinjection.dto.ForgotPasswordForm;
import com.tutorial.spring.dependencyinjection.dto.ResetPasswordForm;
import com.tutorial.spring.dependencyinjection.dto.SignupForm;
import com.tutorial.spring.dependencyinjection.dto.UserDetailsImpl;
import com.tutorial.spring.dependencyinjection.dto.UserEditForm;
import com.tutorial.spring.dependencyinjection.entities.User;
import com.tutorial.spring.dependencyinjection.entities.User.Role;
import com.tutorial.spring.dependencyinjection.mail.MailSender;
import com.tutorial.spring.dependencyinjection.repositories.UserRepository;
import com.tutorial.spring.dependencyinjection.services.UserService;
import com.tutorial.spring.dependencyinjection.util.Utilities;

/*
 * Use Propagation SUPPORTS for read-only transactions
 * Use Propagation REQUIRED for read-write transactions
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private MailSender mailSender;
	
	private static final Log log = LogFactory.getLog(UserServiceImpl.class);
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			MailSender mailSender){
		
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void signup(SignupForm signupForm) {
		/*
		 * final to be sure it is saved
		 */
		final User user = new User();
		
		user.setEmail(signupForm.getEmail());
		user.setName(signupForm.getName());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword().trim()));
		user.getRoles().add(Role.UNVERIFIED);
		user.setVerificationCode(RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH));
		userRepository.save(user);

		/*
		 * Do the verification mail after the transaction to be sure the user is created in the database
		 */
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit(){
				try{
					String verifyLink = Utilities.hostUrl() + "/users/" + user.getVerificationCode() + "/verify";
	
					mailSender.send(user.getEmail(), Utilities.getMessage("verifySubject"), Utilities.getMessage("verifyEmail", verifyLink));
					log.info("Verification email to "+ user.getEmail() + " queued.");
				}catch(MessagingException e){
					log.error(ExceptionUtils.getStackTrace(e));
				}
			}
		});		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		
		if(user == null){
			throw new UsernameNotFoundException(username);
		}
		
		return new UserDetailsImpl(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void verify(String verificationCode){
		long loggedInUserId = Utilities.getSessionUser().getId();
		User user = userRepository.findOne(loggedInUserId);
		
		Utilities.validate(user.getRoles().contains(Role.UNVERIFIED), "alreadyVerified");
		Utilities.validate(user.getVerificationCode().equals(verificationCode), "incorrect", "verification code");
		
		user.getRoles().remove(Role.UNVERIFIED);
		user.setVerificationCode(null);
		userRepository.save(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void forgotPassword(ForgotPasswordForm forgotPasswordForm) {
		final User user = userRepository.findByEmail(forgotPasswordForm.getEmail());
		final String forgotPasswordCode = RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);
		
		user.setForgotPasswordCode(forgotPasswordCode);
		final User savedUser = userRepository.save(user);
		
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit(){
				try{
					mailForgotPasswordLink(savedUser);
				}catch(MessagingException e){
					log.error(ExceptionUtils.getStackTrace(e));
				}
			}
		});	
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result) {
		User user = userRepository.findByForgotPasswordCode(forgotPasswordCode);
		
		if(user == null){
			result.reject("invalidForgotPassword");
		}
		
		if(result.hasErrors()){
			return;
		}
		
		user.setForgotPasswordCode(null);
		user.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword().trim()));
		userRepository.save(user);
	}
	
	private void mailForgotPasswordLink(User user) throws MessagingException{
		String forgotPasswordLink = Utilities.hostUrl() + 
				"/reset-password/" + user.getForgotPasswordCode();
		
		mailSender.send(user.getEmail(), Utilities.getMessage("forgotPasswordSubject"), 
				Utilities.getMessage("forgotPasswordEmail", forgotPasswordLink));
	}

	@Override
	public User findOne(long userId) {
		User loggedIn = Utilities.getSessionUser();
		User user = userRepository.findOne(userId);
		
		if(loggedIn == null ||
				loggedIn.getId() != user.getId() && !loggedIn.isAdmin()){
			/*
			 * Hide the email id
			 */
			user.setEmail("Confidential");
		}
		
		return user;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(long userId, UserEditForm userEditForm){
		User loggedIn = Utilities.getSessionUser();
		Utilities.validate(loggedIn.isAdmin() || loggedIn.getId() == userId,
				"noPermission");
		
		User user = userRepository.findOne(userId);
		user.setName(userEditForm.getName());
		
		if(loggedIn.isAdmin()){
			user.setRoles(userEditForm.getRoles());
		}
		
		userRepository.save(user);
	}
}
