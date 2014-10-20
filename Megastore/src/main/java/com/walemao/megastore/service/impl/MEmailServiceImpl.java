package com.walemao.megastore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.walemao.megastore.service.MEmailService;

@Service
public class MEmailServiceImpl implements MEmailService {

	private static final String fromAddress = "walemao@126.com";
	@Autowired
	private MailSender mailSender;
	
	@Override
	public void sendVericationCode(String username, String emailAddress) {
		// TODO Auto-generated method stub
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom(fromAddress);
		message.setText(username);
		message.setTo(emailAddress);
		message.setSubject("ffffffffffffffffffff");
		message.setSubject("Verification Code");
		mailSender.send(message);
	}

}
