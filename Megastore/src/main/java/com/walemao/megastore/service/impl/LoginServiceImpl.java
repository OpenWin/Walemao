package com.walemao.megastore.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.walemao.megastore.domain.JMessage;
import com.walemao.megastore.domain.User;
import com.walemao.megastore.domain.UserAuthority;
import com.walemao.megastore.domain.UserBase;
import com.walemao.megastore.domain.UserDetail;
import com.walemao.megastore.message.MessageManager;
import com.walemao.megastore.message.define.MessageType;
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

	// private static final String fromAddress = "st3952@163.com";
	private static final String fromAddress = "customer_service@walemao.com";

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

	@Autowired
	private MessageManager messageManager;

	@Override
	public boolean insertUser(User user) {

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

		JMessage jMessage = new JMessage();
		jMessage.setContent("new_user");
		jMessage.setType(MessageType.TYPE1.getTypeValue());
		jMessage.setClusterid("1");
		jMessage.setTarget("queueDistination");
		jMessage.setNext_sendtime(new Timestamp(System.currentTimeMillis()));
		messageManager.addMessage(jMessage);
		messageManager.sendMessage(jMessage);
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
	public void sendVerificationCode(HttpServletRequest request, int code,
			String emailAddress) throws MessagingException,
			UnsupportedEncodingException {
		// TODO Auto-generated method stub
		JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;
		MimeMessage mailMessage = sender.createMimeMessage();
		MimeMessageHelper messageHelper = null;
		messageHelper = new MimeMessageHelper(mailMessage, true);
		String nick = "";
		nick = javax.mail.internet.MimeUtility.encodeText("哇乐猫walemao.com");
		messageHelper.setFrom(new InternetAddress(nick + " <" + fromAddress
				+ ">"));
		messageHelper.setTo(emailAddress);
		messageHelper.setSubject("哇乐猫——帐号验证码");
		messageHelper.setText(
				"<html><head></head><body><h1>hello!!spring image html mail</h1>"
						+ "<a href=http://www.baidu.com>baidu</a>" + "<br/>"
						+ code + "<br/><img src=cid:image/></body></html>",
				true);
		FileSystemResource img = new FileSystemResource(new File(request
				.getSession().getServletContext()
				.getRealPath("/resources/images/1.jpg")));
		messageHelper.addInline("image", img);// 跟cid一致
		sender.send(mailMessage);
		System.out.println("邮件发送成功...");
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		logger.info("sbbbbbbbbbbbbbbbbbbbb " + username);
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
