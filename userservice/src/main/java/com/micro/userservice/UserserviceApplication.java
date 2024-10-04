package com.micro.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.micro.userservice.dto.UserContactInfoDto;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(value = {UserContactInfoDto.class})
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	
}
