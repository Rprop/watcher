/**
 * 
 */
package com.whh.ump.profiler.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.whh.ump.profiler.util.CacheUtil;

/**
 * @author hanming
 * 
 */
public class BusinessModuleTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link BusinessModule#businessHandle(com.whh.ump.profiler.util.CacheUtil, java.lang.String, long, int, int, java.lang.String)}
	 * . 调用该方法后将生成business.log日志文件,断言读取出的内容与预设值一致,detail中不能包含中文字符
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBusinessHandle() throws Exception {
		JsonFactory jfactory = new JsonFactory();
		CacheUtil collector = new CacheUtil();
		String key = "business2";
		long nowTime = 0L;
		int type = 1;
		int value = 0;
		String detail = null;
//		String detail = "如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常。如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您使用代理服务器，请检查您的代理设置或与您的网络管理员联系，以确保代理服务器工作正常如果您认为不应该使用代理服务器，请调整您的代理设置：转至扳手菜单 > 选项 > 高级选项 > 更改代理设置... > LAN 设置，取消选中“为 LAN 使用代理服务器”复选框。,，{}[]:\\u005E \"，\n\r‘’‘’‘\"!\"\"@\",\"#\r\n\"\"$\"\"%\"\"^\",\",&\n\",\",*\"\"(\"\")\"\r\n\"\\\"\"{\"\"}\"\"_\"\"+\"\"[\"\"]\"\"|\"\":\"\"\"<\"\">\"\"?\"\"/\"\";\"\",，还要写一堆中文，为了验证是否能在512个字符处进行截断处理，这个是必须的。｝";
//		String rtxInfo = "hanming,weijiannan";
		String rtxInfo = null;
		String mailInfo = "hanming@360buy.com,weijiannan@360buy.com";
		String smsInfo = "18782189762,18528398472,";
		// System.out.println(detail.length());
		// String detail ="反对萨芬\"计算机发动恐怖";
		// detail = detail.replace("\\", "\\\\");
		// detail = detail.replace("\"","\\\"");
		// BusinessModule.businessHandle(collector, key,
		// nowTime = System.currentTimeMillis(), type, value, detail);

		BusinessModule.businessHandle(key, type, value,	detail, rtxInfo, mailInfo, smsInfo);

		// 获取日志文件所在目录
		File directory = new File(".");
		String rootdir = directory.getCanonicalPath();// 取得当前路径
		String root = rootdir.substring(0, rootdir.indexOf(File.separator));// 取得根目录
		String path = root + File.separator + "export" + File.separator
				+ "home" + File.separator + "tomcat" + File.separator
				+ "UMP-Monitor" + File.separator + "logs";
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.getName().contains("business.log")
						&& (System.currentTimeMillis() - file.lastModified() < 100)) {
					String newpath = path + File.separator + file.getName();
					File newfile = new File(newpath);

					InputStreamReader insReader = new InputStreamReader(
							new FileInputStream(newfile), "UTF-8");

					BufferedReader bufReader = new BufferedReader(insReader);
					// RandomAccessFile reader = null;
					// reader = new RandomAccessFile(newfile, "r");
					String str = bufReader.readLine();
					JsonParser jParser = jfactory.createJsonParser(str);
					String timeValue = null;
					String keyValue = null;
					String hostName = null;
					String detail2 = null;
					String rtxInfo2 = null;
					String mailInfo2 = null;
					String smsInfo2 = null;
					while (jParser.nextToken() != JsonToken.END_OBJECT) {
						String fieldname = jParser.getCurrentName();
						if ("time".equals(fieldname)) {
							jParser.nextToken();
							timeValue = jParser.getText();
						}
						if ("key".equals(fieldname)) {
							jParser.nextToken();
							keyValue = jParser.getText();
						}
						if ("hostname".equals(fieldname)) {
							jParser.nextToken();
							hostName = jParser.getText();
						}
						if ("detail".equals(fieldname)) {
							jParser.nextToken();
							detail2 = jParser.getText();
						}
						if ("RTX".equals(fieldname)) {
							jParser.nextToken();
							rtxInfo2 = jParser.getText();
						}
						if ("MAIL".equals(fieldname)) {
							jParser.nextToken();
							mailInfo2 = jParser.getText();
						}
						if ("SMS".equals(fieldname)) {
							jParser.nextToken();
							smsInfo2 = jParser.getText();
						}
					}
					Assert.assertEquals(rtxInfo, rtxInfo2);
					Assert.assertEquals(mailInfo, mailInfo2);
					Assert.assertEquals(smsInfo, smsInfo2);
					// Assert.assertEquals(512, detail2.length());
				}
			}
		}
	}

}
