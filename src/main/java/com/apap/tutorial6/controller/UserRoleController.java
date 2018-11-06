package com.apap.tutorial6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apap.tutorial6.model.PasswordModel;
import com.apap.tutorial6.model.UserRoleModel;
import com.apap.tutorial6.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		userService.addUser(user);
		return "home";
		}
	
	@RequestMapping(value = "/password/ubah")
	public String updatePassword(){
		return "update-password";
	}
	
	@RequestMapping(value = "/password/ubah", method = RequestMethod.POST)
	public String updatePasswordSubmit(@ModelAttribute PasswordModel password, Model model) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		UserRoleModel user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		
		String message = "";
		if(password.getKonfirmasiPassword().equals(password.getPasswordBaru())) {
			if(passwordEncoder.matches(password.getPasswordLama(), user.getPassword())) {
				message = "Success";
				userService.updatePassword(user, password.getPasswordBaru());
			} else {
				message = "NotMatch";
			}
		} else {
			message = "Failed";
		}
		model.addAttribute("message", message);
		return "update-password";
	}
}
