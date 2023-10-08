package com.luv2code.springboot.demosecurity.controller;

import com.luv2code.springboot.demosecurity.entity.User;
import com.luv2code.springboot.demosecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserService userService;

	@Autowired
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);//ivida init binder white spaces avoid cheyyunnu
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}	
	
	@GetMapping("/showRegistrationForm")
	public String showMyLoginPage(Model theModel) {
		
		theModel.addAttribute("webUser", new User());
		
		return "register/registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(
			@Valid @ModelAttribute("webUser") User theWebUser,
			BindingResult theBindingResult,
			Model theModel) {

		String userName = theWebUser.getUserName();
		
		// form validation
		 if (theBindingResult.hasErrors()){
			 return "register/registration-form";
		 }

		// check the database if user already exists
        User existing = userService.findByUserName(userName);
        if (existing != null){
        	theModel.addAttribute("webUser", new User());
			theModel.addAttribute("registrationError", "User name already exists.");
        	return "register/registration-form";
        }
        
        // create user account and store in the database
        userService.save(theWebUser);

        return "register/registration-confirmation";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("userName") String userName){
		userService.deleteByUserName(userName);
		return "redirect:/systems";
	}

	@GetMapping("/showRegistrationFormForUpdate")
	public String showMyLoginPageForUpdate(@RequestParam("userName") String userName, Model theModel) {

		User user = userService.findByUserName(userName);

		theModel.addAttribute("webUser", user);

		return "register/update-form";
	}

	@PostMapping("/processRegistrationFormForUpdate")
	public String processRegistrationFormForUpdate(
			@Valid @ModelAttribute("webUser") User theWebUser,
			BindingResult theBindingResult,
			Model theModel) {

		// form validation
		if (theBindingResult.hasErrors()){
			return "register/update-form";
		}

		// create user account and store in the database
		userService.saveUp(theWebUser);

		return "register/update-confirmation";
	}
}
