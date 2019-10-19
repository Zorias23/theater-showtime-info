package com.huxley.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.huxley.db.DatabaseUtil;
import com.huxley.generic.Utility;
import com.huxley.model.ConfirmationToken;
import com.huxley.model.EmailSender;
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
	@RequestMapping(value="/login", method = RequestMethod.GET)  //this maps to the URL ending in login.html, we told our servlet to grab everything that's *.html
	public ModelAndView displayLogin(@ModelAttribute ("user") User user, ModelAndView modelAndView)  //should bind to modelAttribute="user" in <form:form> tag in login.jsp
	{
		
		System.out.println("we're in our controller...GET request for login");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("login");
        return modelAndView;

		//return "login";  //this is saying that we're returning login.jsp, located in /WEB-INF/jsp/
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST) 
	public ModelAndView submitLogin(@ModelAttribute ("user") User user, ModelAndView modelAndView)
	{
		System.out.println("we're in our controller...POST request for login");
		User userFromDB;
			userFromDB = DatabaseUtil.verifyUserExists(user.getUserName(), user.getPassword()); //if user exists, AND is verified through their email, this returns true
			if (userFromDB != null)
			{
				modelAndView.addObject("UserObject", userFromDB);
				modelAndView.addObject("UserName", userFromDB.getUserName());
				modelAndView.addObject("Password", userFromDB.getSecurePassword());
				 modelAndView.setViewName("menu");
				
			}
			else
			{
				modelAndView.addObject("errorMessage", "Sorry! Either the username or password is incorrect. Or this user has not been verified through their email address.");
				 modelAndView.setViewName("error");
			}
			return modelAndView;
		
	}
	
	@RequestMapping(value="/signUp", method = RequestMethod.GET)
	public ModelAndView displaySignUp(@ModelAttribute ("user") User user, ModelAndView modelAndView)
	{
		System.out.println("we're in our controller...GET request for signUp");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("signUp");
        return modelAndView;
	}
	
	@RequestMapping(value="/signUp", method = RequestMethod.POST)
	public ModelAndView submitSignUp(@ModelAttribute ("user") User user, ModelAndView modelAndView)
	{
		System.out.println("we're in our controller...POST request for signUp");
		boolean userExists = false;
		userExists = DatabaseUtil.userAlreadyExists(user.getUserName());
		if (userExists == true)
		{
			modelAndView.addObject("errorMessage", "Sorry! That user already exists.  Please try to sign up again with a different username.");
			 modelAndView.setViewName("error_sign_up");
		}
		else
		{
			ConfirmationToken ct = new ConfirmationToken(user);
			user.setConfirmationToken(ct.getConfirmationToken()); //randomly generated confirmationToken was created for the User when that object was instantiated
			DatabaseUtil.createUser(user); //user record and confirmation token record created, now email must be sent to verify account
			String emailMessage = Utility.verifyEmailAccountMessage(user);
			EmailSender email = new EmailSender();
			email.sendEmail(user.getUserName(), emailMessage); //send verification link to user
			modelAndView.addObject("Message", Utility.SUCCESS_SIGNUP_MESSAGE);
			  modelAndView.setViewName("signUpSuccess");
		}
		return modelAndView;
	}
	
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
    	boolean verifyAndComplete = false;
        verifyAndComplete = DatabaseUtil.completeUserRegistration(confirmationToken);

        if(verifyAndComplete == false)
        {
            modelAndView.addObject("errorMessage","This verification link is invalid or broken! Please check the link in your email to make sure it is the proper link and try again.");
            modelAndView.setViewName("error_reg_link");
        }
        else
        {
            modelAndView.addObject("Message","Your account has been successfully verified! You can now login to the application.");
            modelAndView.setViewName("accountVerified");
        }

        return modelAndView;
    }

}
