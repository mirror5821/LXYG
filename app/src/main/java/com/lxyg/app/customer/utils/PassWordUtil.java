package com.lxyg.app.customer.utils;

import dev.mirror.library.utils.MD5Util;

/**
 * 密码加密util
 * @author 王沛栋
 *
 */
public class PassWordUtil {
	/**
	 * md5加密登录
	 * @param password
	 * @return
	 */
	public static String md5PassWord(String password){
		return MD5Util.stringToMd5(password).substring(0, 16);
	}
}
