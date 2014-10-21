package com.walemao.megastore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.domain.UserAuthority;
import com.walemao.megastore.repository.UserAttemptsDao;
import com.walemao.megastore.repository.UserAuthorityDao;
import com.walemao.megastore.repository.UserDao;
import com.walemao.megastore.security.provider.UsernameAuthenticatonProvider;
import com.walemao.megastore.service.MUserAuthorityService;

@Service
public class MUserAuthorityServiceImpl implements MUserAuthorityService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserAttemptsDao userAttemptsDao;
	
	@Autowired
	private UserAuthorityDao userAuthorityDao;
	
	@Autowired
	private UsernameAuthenticatonProvider provider;
	
	@Override
	public boolean registerUser(User user)
	{
		if (userAttemptsDao.CheckUsername(user.getUsername()))
		{
			return false;
		}
		
		String salt = provider.createSaltValue();
		user.setPassword(provider.encodeRegisterPassword(user.getUsername(), user.getPassword(), salt));
		user.setSalt(salt);
		userDao.insert(user);
		
		UserAuthority author = new UserAuthority();
		author.setUsername(user.getUsername());
		author.setAuthority("ROLE_USER");
		userAuthorityDao.insert(author);
		
		
		return true;
	}

	@Override
	@Secured(value="ROLE_USER")
	public boolean changePassword(String username, String oldRawPassword, String newRawPassword) 
	{
		if (!userAttemptsDao.CheckUsername(username))
		{
			System.out.println("check username");
			return false;
		}
		
		if (!provider.isUserPasswordValid(username, oldRawPassword))
		{
			System.out.println("check old password");
			return false;
		}
		
		String saltedPassword = provider.encodeUserPasswordWidthInnerSalt(username, newRawPassword);
		
		userDao.updatePasswd(username, saltedPassword);
		
		return true;
	}
	
	@Override
	public void sendVerificationCode(String usernmae, String emailAddress)
	{
		
	}
}
