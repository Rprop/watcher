/**
 * 
 */
package com.jd.ump.profiler.common;

import java.io.File;
import java.io.RandomAccessFile;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jd.ump.profiler.util.CacheUtil;

/**
 * @author hanming
 *
 */
@Ignore
public class AliveModuleTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link com.jd.ump.profiler.common.AliveModule#run()}.
	 * @throws Exception 
	 */
	@Test
	public void testRun() throws Exception {
		String key = "HeartBeat.test";
		AliveModule aliveModule = new AliveModule(key);
		aliveModule.run();
		
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
				if (file.getName().contains("alive.log")
						&& (System.currentTimeMillis() - file.lastModified() < 20100)) {
					String newpath = path + File.separator + file.getName();
					File newfile = new File(newpath);
					
					RandomAccessFile reader = null;
					reader = new RandomAccessFile(newfile, "r");
					String str=reader.readLine();
//					System.out.println(str);
					String destStr = "{\"key\":\""+key+"\",\"hostname\":\""+CacheUtil.HOST_NAME+"\",\"time\":\"";
					String endstr = "\"}";
					boolean b = str.startsWith(destStr)&&str.endsWith(endstr);
//					System.out.println(b);
					Assert.assertTrue(b);
//					System.out.println(destStr);
				}
			}
		}
	}

}
