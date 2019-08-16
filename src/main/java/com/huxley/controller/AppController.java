package com.huxley.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huxley.model.User;




@Controller
public class AppController {
	
	//probably not going to keep it like this, just quickly trying to get a User object being stored with data
	//after the user attempts to login or register
	@RequestMapping(value="/login")  //this maps to the URL ending in login.html, we told our servlet to grab everything that's *.html
	public String sayHello(@ModelAttribute ("user") User user)  //should bind to modelAttribute="user" in <form:form> tag in login.jsp
	{
		
		System.out.println("we're in our controller...");
		if (user != null)
		{
			if (user.getUserName() != null)
			{
				System.out.println("User now has the userName: " + user.getUserName());
			}
		}
		return "login";  //this is saying that we're returning login.html, located in /WEB-INF/jsp/
	}

}
