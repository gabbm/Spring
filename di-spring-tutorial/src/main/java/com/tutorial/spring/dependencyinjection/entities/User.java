package com.tutorial.spring.dependencyinjection.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.tutorial.spring.dependencyinjection.util.Utilities;

/*
 * Define database entity with his table and Indexes if it is necessary
 */
@Entity
@Table(name="Users", indexes = {
		@Index(columnList = "email", unique = true),
		@Index(columnList = "verificationCode", unique = true),
		@Index(columnList = "forgotPasswordCode", unique = true)
})
public class User {
	public static final int EMAIL_MAX = 250;
	public static final int NAME_MAX = 50;
	public static final int RANDOM_CODE_LENGTH = 16;
	public static final int PASSWORD_MAX = 30;
	public static final String EMAIL_PATTERN = "{A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	
	public static enum Role {
		UNVERIFIED, BLOCKED, ADMIN
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/*
	 * Should be unique
	 */
	@Column(nullable = false, length = EMAIL_MAX)
	private String email;
	
	@Column(nullable = false, length = NAME_MAX)
	private String name;
	
	/*
	 * No length because it will be encrypted
	 */
	@Column(nullable = false, length = PASSWORD_MAX)
	private String password;

	/*
	 * Should be unique
	 */
	@Column(length = RANDOM_CODE_LENGTH)
	private String verificationCode;
	
	/*
	 * Should be unique
	 */
	@Column(length = RANDOM_CODE_LENGTH)
	private String forgotPasswordCode;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<Role>();
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	public String getForgotPasswordCode() {
		return forgotPasswordCode;
	}

	public void setForgotPasswordCode(String forgotPasswordCode) {
		this.forgotPasswordCode = forgotPasswordCode;
	}

	public boolean isAdmin() {
		return roles.contains(Role.ADMIN);
	}
	
	public boolean isEditable(){
		User loggedIn = Utilities.getSessionUser();
		
		if(loggedIn == null ){
			return false;
		}
		
		return loggedIn.isAdmin() ||
				loggedIn.getId() == id;
	}
}
