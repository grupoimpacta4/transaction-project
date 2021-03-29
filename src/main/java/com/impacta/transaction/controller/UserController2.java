package com.impacta.transaction.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
 	

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController 
@EnableAutoConfiguration
public class UserController2 {

     
    @PreAuthorize("hasRole('ROLE_USER')") 
    @RequestMapping(value = "api/teste", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getExtrato(final Principal principal) throws Exception {
		 return "hello";
	}

}
