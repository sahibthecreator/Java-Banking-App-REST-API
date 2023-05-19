package com.bank.app.restapi;

import com.bank.app.restapi.dto.mapper.AccountMapper;
import com.bank.app.restapi.dto.mapper.UserMapper;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public UserMapper userDTOMapper() {
		return new UserMapper(modelMapper());
	}

	@Bean
	public AccountMapper AccountDTOMapper() {
		return new AccountMapper(modelMapper());
	}


	//generates some predefined data in DB - dev version only
	@Bean void createDBData(){

	}
}
