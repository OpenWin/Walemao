package com.walemao.megastore.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.repository.UserDao;
import com.walemao.megastore.security.Exception.SecurityAttributes;
import com.walemao.megastore.security.Exception.SecurityAttributes.LOGIN_ERROR;
import com.walemao.megastore.security.provider.RandomValidateCode;
import com.walemao.megastore.service.MUserService;
import com.walemao.megastore.service.Validation.MRegisterValidator;
import com.walemao.megastore.util.BaseUtil;
import com.walemao.megastore.util.StringMD5;

@Controller
public class MAuthenticationController {
	private Logger logger = LoggerFactory
			.getLogger(MAuthenticationController.class);

	@Autowired
	private MUserService mUserService;

	@Autowired
	private MRegisterValidator usernameValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(usernameValidator);
	}

	/**
	 * 获取登录页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String getLoginPage(
			@CookieValue(value = "_plt", required = false) String pltCookie,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		LOGIN_ERROR loginError = (LOGIN_ERROR) session
				.getAttribute(SecurityAttributes.LOGIN_ERROR_KEY);
		if (loginError != null) {
			request.setAttribute(SecurityAttributes.LOGIN_ERROR_KEY, loginError);
			session.removeAttribute(SecurityAttributes.LOGIN_ERROR_KEY);
			request.setAttribute("username", pltCookie);
		}
		request.setAttribute("username", pltCookie);
		return "login";
	}

	/**
	 * 获取主页页面
	 * 
	 * @param pstCookie
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String getIndexPage(
			@CookieValue(value = "_pst", required = false) String pstCookie,
			HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails user = (UserDetails) principal;
				Cookie foo = new Cookie("_pst", user.getUsername());
				foo.setMaxAge(1209600);
				response.addCookie(foo);
				request.setAttribute("islogin", 1);
				request.setAttribute("username", user.getUsername());
				return "index";
			}
		}

		request.setAttribute("username", pstCookie);
		return "index";
	}

	/**
	 * 获取用户注册页面
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegistrationPage(@ModelAttribute("user") User user) {
		return "/register/registration";
	}

	/**
	 * 用户注册
	 * @param user
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody String processRegistration(
			@Validated @ModelAttribute("user") User user, BindingResult result) {
		if (result.hasErrors()) {
			return result.getFieldErrors().toString();
		}

		if (mUserService.registerUser(user)) {
			return "registration success";
		}
		return "registration failed";
	}
	
	/**
	 * 验证用户名
	 * 
	 * @param username
	 * @return error页面返回连接
	 */
	@RequestMapping(value = "validateuser/isPinEngaged", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> validateUser(String username) {
		Map<String, Object> requstMap = new HashMap<String, Object>();
		if (mUserService.getUser(username) == null) {
			requstMap.put("status", "success");
		} else {
			requstMap.put("status", "error");
		}
		return requstMap;
	}

	

	@RequestMapping(value = "verification", method = RequestMethod.POST)
	public String sendVericationCode(String emailAddress) {
		mUserService.sendVerificationCode("username", emailAddress);
		return "";
	}


}