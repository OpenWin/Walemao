package com.walemao.megastore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.domain.UserAuthority;
import com.walemao.megastore.repository.UserAuthorityDao;
import com.walemao.megastore.repository.UserDao;
import com.walemao.megastore.security.jdbc.UserAttemptsJdbcDaoImpl;
import com.walemao.megastore.security.provider.UsernameAuthenticatonProvider;
import com.walemao.megastore.service.MUserService;

@Service
public class MUserAuthorityServiceImpl implements MUserService {

	private static final String fromAddress = "walemao@126.com";

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserAttemptsJdbcDaoImpl userAttemptsDao;

	@Autowired
	private UserAuthorityDao userAuthorityDao;

	@Autowired
	private UsernameAuthenticatonProvider provider;

	@Autowired
	private MailSender mailSender;

	@Override
	public boolean registerUser(User user) {
		if (userAttemptsDao.CheckUsername(user.getUsername())) {
			return false;
		}

		String salt = provider.createSaltValue();
		user.setPassword(provider.encodeRegisterPassword(user.getUsername(),
				user.getPassword(), salt));
		user.setSalt(salt);
		userDao.insert(user);

		UserAuthority author = new UserAuthority();
		author.setUsername(user.getUsername());
		author.setAuthority("ROLE_USER");
		userAuthorityDao.insert(author);

		return true;
	}

	@Override
	@Secured(value = "ROLE_USER")
	public boolean changePassword(String username, String oldRawPassword,
			String newRawPassword) {
		if (!userAttemptsDao.CheckUsername(username)) {
			System.out.println("check username");
			return false;
		}

		if (!provider.isUserPasswordValid(username, oldRawPassword)) {
			System.out.println("check old password");
			return false;
		}

		String saltedPassword = provider.encodeUserPasswordWidthInnerSalt(
				username, newRawPassword);

		userDao.updatePasswd(username, saltedPassword);

		return true;
	}

	@Override
	public String getUsername(String args, int type) {
		// TODO Auto-generated method stub
		if (type == 0) {
			return userDao.getUserNameByMobilephone(args);
		} else {
			return userDao.getUserNameByEmail(args);
		}
	}

	@Override
	public void sendVerificationCode(String username, String emailAddress) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromAddress);
		message.setText(username);
		message.setTo(emailAddress);
		message.setSubject("ffffffffffffffffffff");
		message.setSubject("Verification Code");
		mailSender.send(message);

	}

	@Override
	public boolean getUsernameExist(String username) {
		// TODO Auto-generated method stub
		return userAttemptsDao.CheckUsername(username);
	}
}
