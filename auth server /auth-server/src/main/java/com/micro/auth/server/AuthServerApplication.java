package com.micro.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	// @Bean
	// public UserDetailsService users() {
	// 	UserDetails user = User.builder()
	// 			.username("user")
	// 			.password("{noop}asd")
	// 			.roles("user")
	// 			.build();
	// 	UserDetails admin = User.builder()
	// 			.username("admin")
	// 			.password("{noop}asd")
	// 			.roles("user", "admin")
	// 			.build();
	// 	return new InMemoryUserDetailsManager(user, admin);
	// }
}
