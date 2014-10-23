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

		if (mUserService.registerUser(user)) {
			return "registration success";
		}
		return "registration failed";
	}

	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public String getLoginPage(
			@CookieValue(value = "foo", required = false) String fooCookie,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		LOGIN_ERROR loginError = (LOGIN_ERROR)session.getAttribute(SecurityAttributes.LOGIN_ERROR_KEY);
		if (loginError != null)
		{
			request.setAttribute(SecurityAttributes.LOGIN_ERROR_KEY, loginError);
			session.removeAttribute(SecurityAttributes.LOGIN_ERROR_KEY);
		}
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
		if (mUserService.changePassword(userDetails.getUsername(), password,
				newPassword)) {
			System.out.println("Modify password sucessfully.");
		}

		return null;
	}

	/**
	 * 找回密码第一步
	 * @return
	 */
	@RequestMapping(value = "findPwd/index", method = RequestMethod.GET)
	public String getFindPasswordIndexPage() {
		return "safe/findPwdPage1";
	}

	/**
	 * 找回密码第二步
	 * @param name		输入内容
	 * @param j_captcha	验证码
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "findPwd/findPwd", method = RequestMethod.POST)
	public String getFindPasswordFindPwdPage(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String j_captcha,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		String message = "";
		try {
			if (RandomValidateCode.isCodeRight(request)) {
				message = "验证码错误";
				redirectAttributes.addFlashAttribute("kaptchaMsg", message);
				return "redirect:/findPwd/index";
			}
			if (name == null || name.length() <= 0
					|| name.equals("用户名/邮箱/已验证手机")) {
				message = "请填写您的用户名/邮箱/已验证手机";
				redirectAttributes.addFlashAttribute("nameMsg", message);
				return "redirect:/findPwd/index";
			} else if (j_captcha == null || j_captcha.length() <= 0) {
				message = "请输入验证码";
				redirectAttributes.addFlashAttribute("kaptchaMsg", message);
				return "redirect:/findPwd/index";
			} else {
				String username = name;
				if (BaseUtil.isEmail(name)) {
					// 邮箱
					username = mUserService.getUsername(name, 1);

				} else if (BaseUtil.isMobile(name)) {
					// 手机号码
					username = mUserService.getUsername(name, 0);
					;
				}
				User user = mUserService.getUser(username);
				if (user == null) {
					message = "您输入的账户名不存在，请核对后重新输入。";
					redirectAttributes.addFlashAttribute("nameMsg", message);
					return "redirect:/findPwd/index";
				}
				if (user.getEmail() != null && !user.getEmail().equals("")) {
					request.setAttribute("encryptemail",
							BaseUtil.encrptEmail(user.getEmail()));
					request.setAttribute("emailMD5",
							StringMD5.longEncode(user.getEmail()));
				}
				if (user.getMobilephone() != null
						&& !user.getMobilephone().equals("")) {
					request.setAttribute("encryptmobilephone",
							BaseUtil.encrptMobilephone(user.getMobilephone()));
					request.setAttribute("mobilephoneMD5",
							StringMD5.longEncode(user.getMobilephone()));
				}
				request.setAttribute("user", user);
				return "safe/findPwdPage2";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "findPwd/resetPassword", method = RequestMethod.POST)
	public String getFindPasswordResetPasswordPage() {
		return "safe/findPwdPage3";
	}

	@RequestMapping(value = "findPwd/resetPasswdSuccess", method = RequestMethod.POST)
	public String getFindPasswordResetPasswdSuccessPage() {
		return "safe/findPwdPage4";
	}

	@RequestMapping(value = "verification", method = RequestMethod.POST)
	public String sendVericationCode(String emailAddress) {
		mUserService.sendVerificationCode("username", emailAddress);
		return "";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(usernameValidator);
	}
	
	/**
	 * 验证用户名
	 * @param username
	 * @return error页面返回连接
	 */
	@RequestMapping(value = "validateuser/isPinEngaged", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> validateUser(String username) {
		Map<String,Object> requstMap = new HashMap<String,Object>();
		if (mUserService.getUser(username) == null) {
			requstMap.put("status", "success");
		} else {
			requstMap.put("status", "error");
		}
		return requstMap;
	}
}