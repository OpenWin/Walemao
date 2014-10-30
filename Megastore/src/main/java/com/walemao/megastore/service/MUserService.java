package com.walemao.megastore.service;

import com.walemao.megastore.domain.User;

public interface MUserService {
	/**
	 * 根据手机号码或邮箱获取用户名
	 * 
	 * @param args
	 * @param type
	 *            0表示手机 1表示邮箱
	 * @return
	 */
	public String getUsername(String args, int type);

	/**
	 * 注册用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean insertUser(User user);

	public boolean updatePassword(String username, String oldRawPassword,
			String newRawPassword);

	public void sendVerificationCode(int code, String emailAddress);

	/**
	 * 根据用户名获取用户信息
	 * 
	 * @param username
	 * @return 为空表示用户名不存在
	 */
	public User getUser(String username);

	/**
	 * 判断手机号码是否存在
	 * 
	 * @param mobilephone
	 * @return
	 */
	public boolean getMobilephoneExist(String mobilephone);

	/**
	 * 判断邮箱是否存在
	 * @param email
	 * @return
	 */
	public boolean getEmailExist(String email);
}
