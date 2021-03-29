package com.impacta.transaction.controller;

 
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
  

import java.security.Principal;
 

@RestController
public class UserController3 {
	
	@PreAuthorize("#oauth2.hasScope('read')")
	 @RequestMapping(value = "/public/me", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
		public String getExtrato(final Principal principal) throws Exception {
			 return "hello";
	}

}
