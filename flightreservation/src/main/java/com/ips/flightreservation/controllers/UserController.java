package com.ips.flightreservation.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.ips.flightreservation.entities.User;
import com.ips.flightreservation.repos.UserRepository;
import com.ips.flightreservation.services.SecurityService;

@Controller
public class UserController 
{	
	private static final Logger LOGGER=LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/showReg")
	public String showRegistrationPage() 
	{	LOGGER.info("Inside showRegistrationPage()");		
		return "login/registerUser";
	}
	
	@RequestMapping("/loginpage")
	public String loginPage() 
	{	
		LOGGER.info("Inside loginPage()");	
		return "login/login";
	}
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") User user) {
		LOGGER.info("Inside register()" + user);
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return "login/login";

	}
	
	@RequestMapping(value="/login" , method=RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap modelMap) {
		LOGGER.info("Inside login() and email is: "+email);	
		boolean loginResponce=securityService.login(email, password);
	//	User user = userRepository.findByEmail(email);
		if(loginResponce) {
			return "findFlights";
		}
		
		else {
			modelMap.addAttribute("msg", "Invalid username or password please try again.");
			
		}
		return "login/login";
	}


}