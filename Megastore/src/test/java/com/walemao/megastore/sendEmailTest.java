package com.walemao.megastore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.walemao.megastore.service.LoginService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/megastore-bean-test.xml")
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
public class sendEmailTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	LoginService mUserService;

	String add1 = "devildomelf@163.com";
	String add2 = "walemao@126.com";

	@Test
	public void Test() {
		String emailAddress = "devildomelf@163.com";
		String username = "ffffffff";
		//mUserService.sendVerificationCode(username, emailAddress);
		System.out.print("send mail");
	}
}
