package com.smartcontactmanager.controller;

import java.util.Random;


import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smartcontactmanager.helper.Message;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.service.EmailService;

@Controller
public class ForgotController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;
	@RequestMapping("/forgot")
	public String forgotForm(Model model) {
		model.addAttribute("title", "Forgot Password - Smart Contact Manager");
		return "forgot_password_form";
	}
	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email,Model model,HttpSession session) {
		model.addAttribute("title", "OTP Verify - Smart Contact Manager");
		Random random = new Random();
		System.out.println("EMAIL: "+email);
		int otp = random.nextInt(999999);
		System.out.println("OTP: "+otp);
		model.addAttribute("email", email);
		
		String subject = "Smart Contact Manager Email Verfication";
		String message = "One Time Passcode:  "+otp+" ";
		String to = email;
		
		boolean flag = this.emailService.sendEmail(to, subject, message);
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}else {
			session.setAttribute("message",new com.smartcontactmanager.helper.Message("Invalid Email Address", "danger"));
			return "forgot_password_form";
			
		}
		
	}
	@PostMapping("verify-otp")
	public String verifyOtp(@RequestParam("uotp") int otp,HttpSession session) {
		int myotp = (int)session.getAttribute("myotp");
		System.out.println("User OTP: "+otp);
		System.out.println("Our OTP: "+myotp);
		String email = (String)session.getAttribute("email");
		if(myotp==otp) {
			User user = this.userRepository.getUserByUserName(email);
			if(user==null) {
				session.setAttribute("message", new Message("User Email Address does not exits", ""));
				return "forgot_password_form";
			}else {
				return "password_change_form";
			}
		}else {
			session.setAttribute("message", new Message("You have entered invalid OTP", ""));
			return "verify-otp";
		}
		
	}
	@PostMapping("change-password")
	public String changePassword(@RequestParam("newpassword") String newPassword,HttpSession session) {
		String email = (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setuPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(user);
		return "redirect:signin?change=Password changed Successfully";
	}
}
