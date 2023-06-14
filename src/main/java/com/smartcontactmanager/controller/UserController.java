package com.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.dao.ContactRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	private User userByUserName;
	private User  user;
	@ModelAttribute
	public void commonData(Model model,Principal principal) {
		System.out.println("Principal: "+principal);
		String name=principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		System.out.println("common data User Object: "+user);
		model.addAttribute("user", user);
	}
	@RequestMapping("/index")
	public String dashbord(Model model,Principal principal) {
		
		model.addAttribute("title", "User Dashboard - Smart Contact Manager");
		return "normal/user_dashbord";
	}
	@GetMapping("/add-contact")
	public String addContact(Model model) {
		model.addAttribute("title", "Add Contact - Smart Contact Manager");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	@PostMapping("/process-contact")
	public String saveContact(@ModelAttribute Contact contact,
			@RequestParam("profile_image") MultipartFile multipartFile,
			Principal principal,
			HttpSession session) {
		//System.out.println("Contact Profile Image: "+multipartFile.getOriginalFilename());
		try {
			
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);
		if(multipartFile.isEmpty()) {
			System.out.println("File is Empty!!");
			contact.setcImage("contact-default.png");
		}else {
			contact.setcImage(multipartFile.getOriginalFilename());
			File file=new ClassPathResource("static/profile_pic/").getFile();
			
			Path path=Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
			Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is Uploaded Successfully");
		}
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);
		System.out.println("Contact Data: "+contact);
		session.setAttribute("message", new Message("You contact added successfully", "success"));
		}catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong, Try again....", "danger"));
		}
		return "normal/add_contact_form";
	}
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model model, Principal principal) {
		model.addAttribute("title", "View Contact - Smart Contact Manager");
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		
		Pageable pageable= PageRequest.of(page,5);
		Page<Contact> contacts=this.contactRepository.findContactByUser(user.getuId(),pageable);
		model.addAttribute("contact", contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		return "normal/show_contact";
	}
	@GetMapping("contact/{cId}")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model,Principal principle) {
		System.out.println("CID: "+cId);
		model.addAttribute("title", "Contact Detail - Smart Contact Manager");
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		String name=principle.getName();
		User user = this.userRepository.getUserByUserName(name);
		if(user.getuId()==contact.getUser().getuId())
			model.addAttribute("contact", contact);
		return "normal/contact_detail";
	}
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, 
			Model model,Principal principal,
			HttpSession session) {
		Contact contact = this.contactRepository.findById(cId).get();
		User user = this.userRepository.getUserByUserName(principal.getName());
		
		if(user.getuId()==contact.getUser().getuId()) {
			user.getContacts().remove(contact);
			this.userRepository.save(user);
		}
		session.setAttribute("message", new Message("Contact Deleted Successfully", "success"));
		return "redirect:/user/show-contacts/0";
	}
	@GetMapping("/edit/{cId}")
	public String editContact(@PathVariable("cId") Integer cId,Model model,Principal principal) {
		Contact contact = this.contactRepository.findById(cId).get();
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Contact Detail - Smart Contact Manager");
		return "normal/edit_contact";
	}
	@PostMapping("/update-contact")
	public String updateContact(@ModelAttribute Contact contact,
			@RequestParam("profile_image") MultipartFile file,
			Model model,HttpSession session, Principal principal) {
			
		try {
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
			
			if(!file.isEmpty()) {
				
				File deleteFile = new ClassPathResource("static/profile_pic").getFile(); 
				File file1 = new File(deleteFile,oldContactDetail.getcImage()); 
				file1.delete();
				 
				
				File saveFile = new ClassPathResource("static/profile_pic").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setcImage(file.getOriginalFilename());
			}else {
				contact.setcImage(oldContactDetail.getcImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Contact Updated Successfully","success"));
		}catch(Exception e) { 
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong","danger"));
			}
		
		return "redirect:/user/contact/"+contact.getcId();
	}
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Contact Detail - Smart Contact Manager");
		return "normal/user_profile";
	}
	@GetMapping("/settings")
	public String openSettings(Model model) {
		model.addAttribute("title", "Settings - Smart Contact Manager");
		return "normal/settings";
	}
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, 
			@RequestParam("newPassword") String newPassword, 
			Principal principal, HttpSession session) {
		String name = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(name);
		System.out.println(oldPassword+" : "+newPassword);
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getuPassword())) {
			currentUser.setuPassword(bCryptPasswordEncoder.encode(newPassword));
			System.out.println("TRANSLATED: "+bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your Password is successfully changed", "success"));
		}else {
			session.setAttribute("message", new Message("Please enter valid old password", "danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
		
	}
}
