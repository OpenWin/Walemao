package com.walemao.megastore;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.walemao.megastore.service.MEmailService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/megastore-bean-test.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class sendEmailTest extends AbstractTransactionalJUnit4SpringContextTests
{

	@Autowired
	MEmailService emailService;
	
	String add1 = "devildomelf@163.com";
	String add2 = "walemao@126.com";
	
	@Test
	public void Test()
	{
		String emailAddress = "devildomelf@163.com";
		String username = "ffffffff";
		emailService.sendVericationCode(username, emailAddress);
		System.out.print("send mail");
	}
}
