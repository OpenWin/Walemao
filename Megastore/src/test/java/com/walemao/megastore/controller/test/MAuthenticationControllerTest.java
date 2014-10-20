package com.walemao.megastore.controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.walemao.megastore.controller.MAuthenticationController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "/megastore-bean-test.xml")
public class MAuthenticationControllerTest {
	private Logger logger = LoggerFactory
			.getLogger(MAuthenticationControllerTest.class);
	private MockMvc mockMvc;

	@Before
	public void step() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(
				new MAuthenticationController()).build();
	}

	@Test
	public void t0_changePassword() throws Exception {
		ResultActions ra = this.mockMvc.perform(MockMvcRequestBuilders
				.post("/user/modify_pwd").accept(MediaType.APPLICATION_JSON)
				.param("password", "1").param("newPassword", "123"));
		
		ra.andDo(MockMvcResultHandlers.print());	
		ra.andExpect(MockMvcResultMatchers.status().isOk());
		ra.andReturn();
	}

}
