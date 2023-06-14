package com.smartcontactmanager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "SignUp - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	@GetMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "Login - Smart Contact Manager");
		return "login";
	}
	@RequestMapping(value="/validation",method = RequestMethod.POST)
	public String validation(@Valid@ModelAttribute("user") User user,BindingResult bindingResult,@RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model model,HttpSession session) {
		model.addAttribute("title", "SignUp - Smart Contact Manager");
		try {
			if(agreement) {
				System.out.println(bindingResult);
				if(bindingResult.hasErrors()) {
					System.out.println("ERROR :"+bindingResult.toString());
					model.addAttribute("user", user);
					return "signup";
				}
				user.setuRole("ROLE_USER");
				user.setuStatus(true);
				user.setuImageUrl("user-default-profile");
				user.setuPassword(this.passwordEncoder.encode(user.getuPassword()));
				User result = this.userRepository.save(user);
				model.addAttribute("user", new User());
				
				session.setAttribute("message", new Message("You're Successfully Registerd ", "alert-success"));
				
				
				
				return "redirect:signup";
			}else {
			System.out.println("You have not agreed the terms and conditions");
			session.setAttribute("message", new Message("You didn't checked terms and condition !! ", "alert-danger"));
			//throw new Exception("You have not agreed the terms and conditions");
			return "redirect:signup";
			}
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !!", "alert-danger_c"));
			return "redirect:signup";
			}
		
	}
}
