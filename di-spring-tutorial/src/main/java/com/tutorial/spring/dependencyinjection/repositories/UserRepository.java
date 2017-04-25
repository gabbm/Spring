package com.tutorial.spring.dependencyinjection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tutorial.spring.dependencyinjection.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	/*
	 * Spring creates it automatically parsing the name
	 * We can use the @Query annotation to write the JPA query
	 * We can also use the @Query annotation to write SQL queries enabling nativeQuery=true
	 * We also can use named parameters instead ?x, example --> :email
	 */
	//User findByEmail(String email);
	@Query("select u from Users u where u.email = ?1")
	User findByEmail(String email);
}
