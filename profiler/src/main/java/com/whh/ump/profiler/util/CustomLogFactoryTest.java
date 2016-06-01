/**
 * 
 */
package com.whh.ump.profiler.util;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author hanming
 * 
 */
public class CustomLogFactoryTest {

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 * 调用该方法产生三个新的日志文件，断言指定路径下的文件数增加3个
	 * @throws Exception
	 */
	@Ignore
	public void testObject() throws Exception {
		// 获取日志文件所在目录
		File directory = new File(".");
		String rootdir = directory.getCanonicalPath();// 取得当前路径
		String root = rootdir.substring(0, rootdir.indexOf(File.separator));// 取得根目录
		String path = root + File.separator + "export" + File.separator
				+ "home" + File.separator + "tomcat" + File.separator
				+ "UMP-Monitor" + File.separator + "logs";
		File dir = new File(path);
		int count1 = 0;// 启动之前文件数目

		if (dir.exists() && dir.isDirectory()) {
			File[] files1 = dir.listFiles();
			for (File file : files1) {
				if (file.getName().contains("alive.log"))
					count1 += 1;
				if (file.getName().contains("tp.log"))
					count1 += 1;
				if (file.getName().contains("business.log"))
					count1 += 1;
			}
		}

		int count2 = 0;// 启动之后文件数目
		if (dir.exists() && dir.isDirectory()) {
			File[] files2 = dir.listFiles();
			for (File file : files2) {
				if (file.getName().contains("alive.log")
						&& (System.currentTimeMillis() - (file.lastModified()) < 100)) {
					count2 += 1;
					// System.out.println(file.getName());
				} else if (file.getName().contains("tp.log")
						&& (System.currentTimeMillis() - (file.lastModified()) < 100)) {
					count2 += 1;
					// System.out.println(file.getName());
				} else if (file.getName().contains("business.log")
						&& (System.currentTimeMillis() - (file.lastModified()) < 100)) {
					count2 += 1;
					// System.out.println(file.getName());
				} else {
					count2 += 1;
				}
			}
		}

		Assert.assertEquals(3,count2 - count1 );
//		System.out.println(count1 + " " + count2);
	}

}
