package com.jd.jmi.escort.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * API开放中心参数签名摘要算法<br/>
 * @author cdlijunlin
 */
public final class JopApiDigest {
	
	private static final Logger LOG = LoggerFactory.getLogger(JopApiDigest.class);

	private MessageDigest digest;
	
	private JopApiDigest() {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException ex) {
			throw new InternalError("init MessageDigest error:" + ex.getMessage());  
		}
	}
	
	public static JopApiDigest getInstance() {
		return new JopApiDigest();
	}
	
	/**
	 * 使用MD5加密
	 * @param data
	 * @return
	 */
	public String encryptByMD5(String data) {
		if (null == data || "".equals(data)) {
			return "";
		}
		// 将传入字符串转换为字符数组
		char[] chars = data.toCharArray();
		byte[] bytes = new byte[chars.length];
		// 将字符数组转化为字节数组
		for (int i = 0; i < chars.length; i++) {
			bytes[i] = (byte) chars[i];
		}
		// 使用指定的字节更新摘要，并获得密文
		byte[] md5Bytes = digest.digest(bytes);
		StringBuilder result = new StringBuilder();
		// 把密文转换成十六进制的字符串形式
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (16 > val) {
				result.append("0");
			}
			result.append(Integer.toHexString(val));
		}
		return result.toString();
	}
	
	/**
	 * 生成一个签名
	 * @param appKey 分发给业务系统的标识(相当于erp账号)
	 * @param token  appKey密码
	 * @param timestamp 时间戳
	 * @param random 随机数
	 * @return
	 */
	public String generate(String appKey, String token, String timestamp, String random) {
		//将需要签名的参数进行字典排序
		String[] arrTmp = {appKey, token, timestamp, random};
		Arrays.sort(arrTmp);
		//将进行签名的排序后的参数直接拼接成一个字符串进行MD5加密
		StringBuilder sb = new StringBuilder();
		for (String str : arrTmp) {
			sb.append(str);
		}
		return encryptByMD5(sb.toString());
	}
	
	/**
	 * 验证参数签名
	 * @param signature 业务系统通过http请求传送过来的签名
	 * @param appKey 分发给业务系统的标识(相当于erp账号)
	 * @param token appKey密码
	 * @param timestamp 时间戳
	 * @param random 随机数
	 * @return
	 */
	public boolean validate(String signature, String appKey, String token, String timestamp, String random) {
		boolean flag = false;
		String expectedSignature = generate(appKey, token, timestamp, random);
		if (expectedSignature.equals(signature)) {
			flag = true;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("JopApiDigest validate parameter list:signature={},expectedSignature={},appKey={},token={},timstamp={},random={}", 
					new Object[]{signature,expectedSignature,appKey,token,timestamp,random});
		}
		return flag;
	}
}