package com.huxley.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AppController {
	
	//not necessarily going to keep 'greeting' here, just pasted some code
	@RequestMapping(value="/greeting")  //this maps to the URL ending in greeting.html, we told our servlet to grab everything that's *.html
	public String sayHello(Model model)  //Model is a kind of HashMap, with key-value pairs inside of it
	{
		model.addAttribute("greeting", "Hello Robert");
		//@ModelAttribute is used with HTTP GET and POST, can used to get and send data to and from controller
		return "hello";  //this is saying that we're returning hello.jsp
	}

}
