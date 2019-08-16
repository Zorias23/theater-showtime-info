package com.huxley.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AppController {
	
	//not necessarily going to keep 'greeting' here, just pasted some code
	@RequestMapping(value="/login")  //this maps to the URL ending in login.html, we told our servlet to grab everything that's *.html
	public String sayHello(Model model)  //Model is a kind of HashMap, with key-value pairs inside of it
	{
		model.addAttribute("greeting", "Hello Robert");
		System.out.println("we're in our controller...");
		return "login";  //this is saying that we're returning login.html, located in /WEB-INF/jsp/
	}

}
