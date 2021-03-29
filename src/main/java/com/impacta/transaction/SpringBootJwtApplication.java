package com.impacta.transaction;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.impacta.transaction.repository.TransactionRepository;

@EnableAutoConfiguration
@ComponentScan({"com","com.impacta","com.impacta","com.impacta.transaction.service","com.impacta.transaction.repository","com.impacta.transaction.controller","com.impacta.transaction.repository","com.impacta.transaction.model"})
@EnableJpaRepositories(basePackages = {"com.impacta.transaction.repository"} )
@SpringBootApplication(scanBasePackages = {"com.impacta","com.impacta.transaction.service","com.impacta.transaction.repository","com.impacta.transaction.controller"})
public class SpringBootJwtApplication {
	
	  public static void main(String[] args) {
		SpringApplication.run(SpringBootJwtApplication.class, args);
	}
	
	
	

	 
}
