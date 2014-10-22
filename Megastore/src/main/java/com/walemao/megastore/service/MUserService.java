package com.walemao.megastore.service;

import com.walemao.megastore.domain.User;

public interface MUserService {
	
	public boolean getUsernameExist(String username);
	
	public String getUsername(String args, int type);

	public boolean registerUser(User user);

	public boolean changePassword(String username, String oldRawPassword,
			String newRawPassword);

	public void sendVerificationCode(String usernmae, String emailAddress);
}
