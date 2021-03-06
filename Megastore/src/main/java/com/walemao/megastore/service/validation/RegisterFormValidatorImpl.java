package com.walemao.megastore.service.validation;

import org.springframework.stereotype.Component;

import com.walemao.megastore.domain.ErrorParamOut;
import com.walemao.megastore.util.ValidateUtil;

@Component
public class RegisterFormValidatorImpl implements RegisterFormValidator
{
	private final static String USERNAME_EMPTY = "请填写用户名";
	private final static String USERNAME_ERROR = "用户名只能包含数字和字母";
	
	private final static String PASSWORD_EMPTY = "请填写密码";
	private final static String PASSWORD_ERROR = "请包含数字和字母";
	
	private final static String CONFIRM_PASSWORD_EMPTY = "请填写确认密码";
	private final static String CONFIRM_PASSWORD_ERROR = "确认密码跟密码不一致";
	
	private final static String PHONE_EMPTY = "请填写电话";
	private final static String PHONE_ERROR = "电话错误";
	
	private final static String EMAIL_EMPTY = "请填写邮箱";
	private final static String EMAIL_ERROR = "请填写有效邮箱";
	
	private final static String VERIFY_CODE_UNKNOWN = "请获取验证码";
	private final static String VERIFY_CODE_EMPTY = "请填写验证码";
	private final static String VERIFY_CODE_ERROR = "验证码错误";

	@Override
	public boolean CheckUserName(String username, ErrorParamOut errorOut) {
		
		if (username == null || username.isEmpty())
		{
			errorOut.setError(USERNAME_EMPTY);
			return false;
		}
		
		if (!ValidateUtil.validateUsername(username))
		{
			errorOut.setError(USERNAME_ERROR);
			return false;
		}

		return true;
	}

	@Override
	public boolean CheckPassword(String password, String confirmPassWord,
			ErrorParamOut errorOut) {
		if (password == null || password.isEmpty())
		{
			errorOut.setError(PASSWORD_EMPTY);
			return false;
		}
		
		if (!ValidateUtil.validatePassword(password))
		{
			errorOut.setError(PASSWORD_ERROR);
			return false;
		}
		
		if (confirmPassWord == null || confirmPassWord.isEmpty())
		{
			errorOut.setError(CONFIRM_PASSWORD_EMPTY);
			return false;
		}
		else if (!password.equals(confirmPassWord))
		{
			errorOut.setError(CONFIRM_PASSWORD_ERROR);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean CheckEmail(String email, ErrorParamOut errorOut) {
		
		if (email == null || email.isEmpty())
		{
			errorOut.setError(EMAIL_EMPTY);
			return false;
		}
		
		if (!ValidateUtil.isEmail(email))
		{
			errorOut.setError(EMAIL_ERROR);
			return false;
		}
		return true;
	}

	@Override
	public boolean CheckPhone(String phone, ErrorParamOut errorOut) 
	{
		if (phone == null || phone.isEmpty())
		{
			errorOut.setError(PHONE_EMPTY);
			return false;
		}
		
		if (!ValidateUtil.isMobile(phone))
		{
			errorOut.setError(PHONE_ERROR);
			return false;
		}
		return true;
	}

	@Override
	public boolean CheckVerifyCode(String generatedCode, String formCode,
			ErrorParamOut errorOut) {
		
		if (formCode == null || formCode.isEmpty()) 
		{
			errorOut.setError(VERIFY_CODE_EMPTY);
			return false;
		} 
		else if (!formCode.equals(generatedCode)) 
		{
			errorOut.setError(VERIFY_CODE_ERROR);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean CheckVerifyCode(Object generatedCode, String formCode,
			ErrorParamOut errorOut) 
	{
		if (generatedCode == null || generatedCode.toString().isEmpty())
		{
			errorOut.setError(VERIFY_CODE_UNKNOWN);
			return false;
		}
		
		return CheckVerifyCode(generatedCode.toString(), formCode, errorOut);
	}

	@Override
	public boolean CheckRegister(String username, String password, String confirmPassword,
			String email, String phone, Object generatedCode, String formCode,
			ErrorParamOut errorOut) {
		// TODO Auto-generated method stub
		
		if (!CheckUserName(username, errorOut) || !CheckPassword(password, confirmPassword, errorOut))
		{
			return false;
		}
		
		if (!CheckPhone(phone, errorOut) && !CheckEmail(email, errorOut))
		{
			return false;
		}
		
		return CheckVerifyCode(generatedCode, formCode, errorOut);
	}
}
