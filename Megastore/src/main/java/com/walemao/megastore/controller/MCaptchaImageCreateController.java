package com.walemao.megastore.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Producer;
import com.walemao.megastore.security.provider.RandomValidateCode;

@Controller
public class MCaptchaImageCreateController {

	private Producer captchaProducer = null;

	@Autowired
	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}

	@RequestMapping(value = "captcha", method = RequestMethod.GET)
	public String getCheckPage() {
		System.out.println("get checkPage");
		return "captcha";
	}

	@RequestMapping(value = "/kaptcha.jpg")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RandomValidateCode.getRandcode(request, response);
		return null;
	}

	@RequestMapping(value = "check", method = RequestMethod.POST)
	@ResponseBody
	public String loginCheck(HttpServletRequest request,
	/*
	 * @RequestParam(value = "username", required = true) String username,
	 * 
	 * @RequestParam(value = "password", required = true) String password,
	 */
	@RequestParam(value = "kaptcha", required = true) String kaptchaReceived) {

		String kaptchaExpected = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

		if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) {
			return "kaptcha_error";
		}

		return "success";
	}
}
