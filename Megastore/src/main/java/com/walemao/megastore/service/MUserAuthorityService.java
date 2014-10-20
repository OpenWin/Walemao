package com.walemao.megastore.service;

import com.walemao.megastore.domain.User;

public interface MUserAuthorityService 
{	
	public boolean registerUser(User user);
	
	public boolean changePassword(String username, String oldRawPassword, String newRawPassword);
	
	public void sendVerificationCode(String usernmae, String emailAddress);
}
