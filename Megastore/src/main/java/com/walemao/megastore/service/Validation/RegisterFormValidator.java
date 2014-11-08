package com.walemao.megastore.service.validation;

import com.walemao.megastore.domain.ErrorParamOut;

public interface RegisterFormValidator {

	boolean CheckUserName(String username, ErrorParamOut errorOut);
	
	boolean CheckPassword(String password, String confirmPassWord, ErrorParamOut errorOut);
	
	boolean CheckEmail(String email, ErrorParamOut errorOut);
	
	boolean CheckPhone(String phone, ErrorParamOut errorOut);
	
	boolean CheckVerifyCode(String generatedCode, String formCode, ErrorParamOut errorOut);
	
	boolean CheckVerifyCode(Object generatedCode, String formCode, ErrorParamOut errorOut);
	
	boolean CheckRegister(String username,
			String password,
			String confirmPassword,
			String email,
			String phone,
			Object generatedCode,
			String formCode,
			ErrorParamOut errorOut);
}
