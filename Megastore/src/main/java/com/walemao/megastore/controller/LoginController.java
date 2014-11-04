package com.walemao.megastore.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.TextMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ytx.org.apache.http.HttpResponse;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.security.filter.LoginAuthenticationFilter;
import com.walemao.megastore.security.provider.RandomValidateCode;
import com.walemao.megastore.security.util.LoginAttributeJudge;
import com.walemao.megastore.security.util.XmlBeanSpringContextHelper;
import com.walemao.megastore.service.LoginService;
import com.walemao.megastore.service.Validation.RegisterFormValidator;
import com.walemao.megastore.service.Validation.RegisterFormValidatorImpl;
import com.walemao.megastore.service.jms.JmsPushTest;
import com.walemao.megastore.util.BaseUtil;
import com.walemao.megastore.util.ErrorParamOut;

@Controller
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	private final static String VERIFY_CODE_ATT = "VERIFY_CODE_ATT";

	@Autowired
	private LoginService mUserService;

	@Autowired
	private RegisterFormValidatorImpl usernameValidator;

	@Autowired
	private JmsPushTest jmsPushTest;
	@Autowired
	@Qualifier("queueDestination")
	private Destination destination;

	@Autowired
	@Qualifier("topicDestination")
	private Destination topicDestination;
	
	@Autowired
	private RegisterFormValidator registerValidator;

	private LoginAuthenticationFilter loginFilter;

	public LoginAuthenticationFilter getLoginFilter() {
		if (loginFilter == null) {
			loginFilter = (LoginAuthenticationFilter) XmlBeanSpringContextHelper
					.getBean("loginAuthenticaionFilter");
		}
		return loginFilter;
	}

	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kaptcha.jpg")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RandomValidateCode.getRandcode(request, response);
		return null;
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
		LoginAttributeJudge.RedirectSessionAttribute(request);

		//jmsPushTest.pushMessage(destination, "Hello World!");

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
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegistrationPage(@ModelAttribute("user") User user) {
		//jmsPushTest.pushMessage(topicDestination, "Topic message...");
		return "/register/registration";
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegistration(
			@RequestParam(required = false) String authCode,
			@Validated @ModelAttribute("user") User user,
			HttpServletRequest request, HttpServletResponse response,
			BindingResult result, RedirectAttributes redirectAttributes) {

		String errorRedirectUrl = "redirect:/register";
		String successRedirectUrl = "redirect:/index";

		if (result.hasErrors()) {
			// return result.getFieldError().toString();
			redirectAttributes.addFlashAttribute("result",
					result.getAllErrors());
			return errorRedirectUrl;
		}
		// 验证短信或邮箱验证码
		Object codeAttri = request.getSession().getAttribute(VERIFY_CODE_ATT);
		ErrorParamOut resultMsg = new ErrorParamOut();
		if (!registerValidator.CheckRegister(user.getUsername(), 
				user.getPassword(), 
				user.getConfirmPassword(), 
				user.getEmail(), 
				user.getMobilephone(),
				codeAttri,
				authCode, resultMsg))
		{
			System.out.println(resultMsg.getError());
			redirectAttributes.addFlashAttribute("erroCode", resultMsg.getError());
			return errorRedirectUrl;
		}

		try 
		{
			if (!mUserService.insertUser(user)) 
			{
				redirectAttributes.addFlashAttribute("erroCode", "用户已存在");
				return errorRedirectUrl;
			} 
			else if (getLoginFilter().registToLoginFilter(request, response))
			{
				return successRedirectUrl;
			}

		} 
		catch (Exception e) 
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("erroCode", "注册失败！");
		return errorRedirectUrl;
	}

	/**
	 * 验证用户名
	 * 
	 * @param username
	 * @return error页面返回连接
	 */
	@RequestMapping(value = "validateuser/isPinEngaged", method = RequestMethod.GET)
	public @ResponseBody String validateUser(String username)
	{
		ErrorParamOut errorOut = new ErrorParamOut();
		if (!registerValidator.CheckUserName(username, errorOut))
		{
			return errorOut.getError();
		}
		
		if (mUserService.getUser(username) == null) {
			return "success";
		}
		return "error";
	}

	/**
	 * 验证手机
	 * 
	 * @param mobilephone
	 * @return
	 */
	@RequestMapping(value = "validateuser/isPinMobilePhone", method = RequestMethod.GET)
	public @ResponseBody String validateMobilephone(String mobilephone) 
	{
		ErrorParamOut errorOut = new ErrorParamOut();
		if (!registerValidator.CheckPhone(mobilephone, errorOut))
		{
			return errorOut.getError();
		}
		
		if (mUserService.getMobilephoneExist(mobilephone)) {
			return "success";
		}
		return "error";
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "validateuser/isPinEmail", method = RequestMethod.GET)
	public @ResponseBody String validateEmail(String email)
	{
		ErrorParamOut errorOut = new ErrorParamOut();
		if (!registerValidator.CheckEmail(email, errorOut))
		{
			return errorOut.getError();
		}
		if (mUserService.getEmailExist(email)) {
			return "success";
		}
		return "error";
	}

	/**
	 * 发送邮箱验证码
	 * 
	 * @param emailAddress
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "sendEmCode", method = RequestMethod.POST)
	public @ResponseBody String sendVericationCode(String emailAddress, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setCharacterEncoding("utf-8");

		ErrorParamOut errorOut = new ErrorParamOut();
		if (!registerValidator.CheckEmail(emailAddress, errorOut))
		{
			return errorOut.getError();
		}
		
		if (mUserService.getEmailExist(emailAddress)) {
			return "邮箱已被注册过了，请更换其他邮箱！".toString();
		}
		int code = BaseUtil.random();
		
		try 
		{
			mUserService.sendVerificationCode(code, emailAddress);
			request.getSession().setAttribute(VERIFY_CODE_ATT, code);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 发送手机验证码
	 * 
	 * @param mobilephone
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "sendMpCode", method = RequestMethod.POST)
	public @ResponseBody String sendMobilephoneVericationCode(
			String mobilephone, HttpServletRequest request) {
		
		ErrorParamOut errorOut = new ErrorParamOut();
		if (!registerValidator.CheckPhone(mobilephone, errorOut))
		{
			return errorOut.getError();
		}
		
		if (mUserService.getMobilephoneExist(mobilephone)) {
			return "手机已被注册过了，请更换其他手机！";
		}
		int code = BaseUtil.random();
		logger.info("打印验证码：" + code);
		request.getSession().setAttribute(VERIFY_CODE_ATT, code);
		/*// 接口切换 SmsIhuyiImpl 入门高，SmsWeimiImpl低 偏贵
		Sms sms = new SmsYuntongxunImpl();
		// Sms sms = new SmsWeimiImpl();
		Map<String, Object> map;
		try {
			map = sms.excute(code, mobilephone, 0);
			if (map.get("status").equals("success")) {
				return "success";
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return "success";
	}

}