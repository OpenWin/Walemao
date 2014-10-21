package com.walemao.megastore.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.service.MEmailService;
import com.walemao.megastore.service.MUserAuthorityService;
import com.walemao.megastore.service.Validation.MRegisterValidator;

@Controller
public class MAuthenticationController {
	private Logger logger = LoggerFactory
			.getLogger(MAuthenticationController.class);

	@Autowired
	private MUserAuthorityService userAuthorityService;

	@Autowired
	private MRegisterValidator usernameValidator;

	@Autowired
	private MEmailService emailService;

	// Display the form on the get request
	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String getRegistrationPage(@ModelAttribute("user") User user) {
		return "registration";
	}

	// Process the form.
	@RequestMapping(value = "reg", method = RequestMethod.POST)
	public @ResponseBody String processRegistration(
			@Validated @ModelAttribute("user") User user, BindingResult result) {
		if (result.hasErrors()) {
			return result.getFieldErrors().toString();
		}

		if (userAuthorityService.registerUser(user)) {
			return "registration success";
		}
		return "registration failed";
	}

	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String getLoginPage(
			@CookieValue(value = "foo", required = false) String fooCookie,
			HttpServletRequest request) {

		request.setAttribute("username", fooCookie);
		return "login";
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String getIndexPage(
			@CookieValue(value = "foo", required = false) String fooCookie,
			HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails user = (UserDetails) principal;
				Cookie foo = new Cookie("foo", user.getUsername());
				foo.setMaxAge(1209600);
				response.addCookie(foo);
				request.setAttribute("islogin", 1);
				request.setAttribute("username", user.getUsername());
				return "index";
			}
		}
		
		request.setAttribute("username", fooCookie);
		return "index";	
	}

	@RequestMapping(value = "modify_pwd", method = RequestMethod.GET)
	public String getModifyPasswordPage() {
		return "modify_pwd";
	}

	@RequestMapping(value = "user/modify_pwd", method = RequestMethod.POST)
	public @ResponseBody String changePassword(String password,
			String newPassword) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		if (null == userDetails || userDetails.getUsername().length() <= 0)
			return "failed";

		System.out.println(userDetails.getUsername() + " -modify_pwd ");
		System.out.println("password:" + password + "\nnew password:"
				+ newPassword);
		if (userAuthorityService.changePassword(userDetails.getUsername(),
				password, newPassword)) {
			System.out.println("Modify password sucessfully.");
		}

		return null;
	}

	@RequestMapping(value = "forget_pwd", method = RequestMethod.GET)
	public String getFindPasswordVerificationPage() {
		return "find_pwd_verification";
	}

	@RequestMapping(value = "verification", method = RequestMethod.POST)
	public String sendVericationCode(String emailAddress) {
		emailService.sendVericationCode("username", emailAddress);
		return "";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(usernameValidator);
	}
}