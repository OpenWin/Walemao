package com.walemao.megastore.service.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.util.BaseUtil;

@Component
public class MRegisterValidator implements Validator {
	private Logger logger = LoggerFactory.getLogger(MRegisterValidator.class);

	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors e) {
		User user = (User) target;

		ValidationUtils.rejectIfEmpty(e, "username", "username.empty",
				"用户名不能为空");

		ValidationUtils
				.rejectIfEmpty(e, "password", "password.empty", "密码不能为空");

		ValidationUtils.rejectIfEmpty(e, "mobilephone", "mobilephone.empty",
				"手机号码不能为空");

		if (!(user.getPassword()).equals(user.getConfirmPassword())) {
			e.rejectValue("password", "matchingPassword.registration.password",
					"Password and Confirm Password Not match.");
		}
		if (!BaseUtil.isMobile(user.getMobilephone())) {
			e.rejectValue("mobilephone", "mobilephone.error","手机号码格式错误");
		}
	}
}
