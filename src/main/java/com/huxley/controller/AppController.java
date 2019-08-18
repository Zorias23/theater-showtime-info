package com.huxley.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huxley.db.DatabaseUtil;
import com.huxley.model.User;




@Controller
public class AppController {
	
	@RequestMapping(value="/") 
	public String goToLogin()
	{
		return "login";
	}
	
	@RequestMapping(value="/index") 
	public String goToLogin2()
	{
		return "login";
	}
	
	//probably not going to keep it like this, just quickly trying to get a User object being stored with data
	//after the user attempts to login or register
	@RequestMapping(value="/login")  //this maps to the URL ending in login.html, we told our servlet to grab everything that's *.html
	public String sayHello(@ModelAttribute ("user") User user, Model model)  //should bind to modelAttribute="user" in <form:form> tag in login.jsp
	{
		
		System.out.println("we're in our controller...");
		User userFromDB;
		if (user != null && user.getUserName() != null && user.getPassword() != null)
		{
			userFromDB = DatabaseUtil.verifyUserExists(user.getUserName(), user.getPassword()); //if object is populated, user is sucessfully found
			if (userFromDB != null)
			{
				model.addAttribute("UserObject", userFromDB);
				model.addAttribute("UserName", userFromDB.getUserName());
				model.addAttribute("Password", userFromDB.getSecurePassword());
				return "menu";
			}
			
		}
		return "login";  //this is saying that we're returning login.jsp, located in /WEB-INF/jsp/
	}
	
	@RequestMapping(value="/signUp")
	public String signUp(@ModelAttribute ("user") User user, Model model)
	{
		if (user != null && user.getUserName() != null && user.getPassword() != null)
		{
			DatabaseUtil.createUser(user);
			model.addAttribute("UserObject", user);
			model.addAttribute("UserName", user.getUserName());
			model.addAttribute("Password", user.getSecurePassword());
			return "menu";
		}
		return "signUp";
	}

}
