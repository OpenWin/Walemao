package com.walemao.megastore.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.walemao.megastore.domain.User;
import com.walemao.megastore.domain.UserAuthority;
import com.walemao.megastore.domain.UserBase;
import com.walemao.megastore.domain.UserDetail;
import com.walemao.megastore.repository.UserAuthorityDao;
import com.walemao.megastore.repository.UserBaseDao;
import com.walemao.megastore.repository.UserDao;
import com.walemao.megastore.repository.UserDetailDao;
import com.walemao.megastore.security.jdbc.UserAttemptsJdbcDaoImpl;
import com.walemao.megastore.security.provider.UsernameAuthenticatonProvider;
import com.walemao.megastore.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	private static final String fromAddress = "walemao@126.com";

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserBaseDao userBaseDao;

	@Autowired
	private UserDetailDao userDetailDao;

	@Autowired
	private UserAttemptsJdbcDaoImpl userAttemptsDao;

	@Autowired
	private UserAuthorityDao userAuthorityDao;

	@Autowired
	private UsernameAuthenticatonProvider provider;

	@Autowired
	private MailSender mailSender;

	@Override
	public boolean insertUser(User user) {
		logger.info("开始注册~~~~~~~");
		if (userAttemptsDao.CheckUsername(user.getUsername())) {
			return false;
		}

		String salt = provider.createSaltValue();
		user.setPassword(provider.encodeRegisterPassword(user.getUsername(),
				user.getPassword(), salt));
		user.setSalt(salt);
		userDao.insert(user);

		UserBase userBase = new UserBase();
		userBase.setUsername(user.getUsername());
		if (user.getMobilephone() != null) {
			userBase.setIsval_mobilephone(true);
		}
		if (user.getEmail() != null) {
			userBase.setIsval_email(true);
		}
		userBaseDao.insert(userBase);

		UserDetail userDetail = new UserDetail();
		userDetail.setUsername(user.getUsername());
		userDetailDao.insert(userDetail);

		UserAuthority author = new UserAuthority();
		author.setUsername(user.getUsername());
		author.setAuthority("ROLE_USER");
		userAuthorityDao.insert(author);

		return true;
	}

	@Override
	@Secured(value = "ROLE_USER")
	public boolean updatePassword(String username, String oldRawPassword,
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
			return userAttemptsDao.getUserNameByMobilephone(args);
		} else {
			return userAttemptsDao.getUserNameByEmail(args);
		}
	}

	@Override
	public void sendVerificationCode(int code, String emailAddress) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromAddress);
		message.setText("" + code);
		message.setTo(emailAddress);
		message.setSubject("哇乐猫——帐号验证码");
		mailSender.send(message);
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		logger.info("sbbbbbbbbbbbbbbbbbbbb "+username);
		return userDao.getUser(username);
	}

	@Override
	public boolean getMobilephoneExist(String mobilephone) {
		// TODO Auto-generated method stub
		return userAttemptsDao.CheckMobilephone(mobilephone);
	}

	@Override
	public boolean getEmailExist(String email) {
		// TODO Auto-generated method stub
		return userAttemptsDao.CheckEmail(email);
	}

}
