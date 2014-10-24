package com.walemao.megastore.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.security.provider.RandomValidateCode;
import com.walemao.megastore.service.MUserService;
import com.walemao.megastore.util.BaseUtil;
import com.walemao.megastore.util.StringMD5;

@Controller
@RequestMapping(value = "/safety")
public class MSafetyController {
	private Logger logger = LoggerFactory.getLogger(MSafetyController.class);
	
	@Autowired
	private MUserService mUserService;
	
	/**
	 * 获取修改密码页面
	 * @return
	 */
	@RequestMapping(value = "/modify/pwd", method = RequestMethod.GET)
	public String getModifyPasswordPage() {
		return "/safe/modifyPwd/modifyPwd";
	}

	/**
	 * 修改密码
	 * @param password
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/modify/pwd", method = RequestMethod.POST)
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
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findPwd", method = RequestMethod.GET)
	public String getFindPasswordIndexPage() {
		return "safe/findPwd/findPwdPage1";
	}

	/**
	 * 找回密码第二步
	 * 
	 * @param name
	 *            输入内容
	 * @param j_captcha
	 *            验证码
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/findPwd/validation", method = RequestMethod.POST)
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
				return "redirect:/safe/findPwd";
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
				return "safety/findPwd/findPwdPage2";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/findPwd/resetPassword", method = RequestMethod.POST)
	public String getFindPasswordResetPasswordPage() {
		return "safety/findPwd/findPwdPage3";
	}

	@RequestMapping(value = "/findPwd/resetPasswdSuccess", method = RequestMethod.POST)
	public String getFindPasswordResetPasswdSuccessPage() {
		return "safety/findPwd/findPwdPage4";
	}
}
