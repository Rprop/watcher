package com.jd.ump.profiler;

import java.io.File;
import java.io.RandomAccessFile;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jd.ump.profiler.util.CacheUtil;

@Ignore
public class CallerInfoTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * 调用该方法后将生成tp.log日志文件,断言读取出的内容与预设值一致
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStop() throws Exception {
		long startTime = System.currentTimeMillis();
		long elapsedTime = 10L;
		String key = " lifang.test.aaa ";
		boolean enableTP = true;

		CacheUtil collector = new CacheUtil();
		CallerInfo info = new CallerInfo(key);
		info.stop();

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
				if (file.getName().contains("tp.log")
						&& (System.currentTimeMillis() - file.lastModified() < 100)) {
					String newpath = path + File.separator + file.getName();
					File newfile = new File(newpath);

					RandomAccessFile reader = null;
					reader = new RandomAccessFile(newfile, "r");
					String str = reader.readLine();
//					 System.out.println(str);
					String destStr = "{\"key\":\"" + key + "\",\"hostname\":\""
							+ collector.HOST_NAME
							+ "\",\"processState\":\"0\",\"time\":\""
							+ collector.changeLongToDate(startTime)
							+ "\",\"elapsedTime\":\"";
					String endstr = "\"}";
					boolean b = str.startsWith(destStr) && str.endsWith(endstr);
//					 System.out.println(b);
					Assert.assertTrue(b);
//					 System.out.println(destStr);
				}
			}
		}
	}

}
