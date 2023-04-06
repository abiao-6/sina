package com.abiao.sina.common.utils;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * 
     * Title: CallShell.java    
     * Description: 执行shell命令的工具类
 */
public class CallShell {
 
	public static String call(String[] str) throws IOException {
		if (str == null || str.length == 0) {
			return null;
		}
		Process process = Runtime.getRuntime().exec(str);
		BufferedReader strCon = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = strCon.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		strCon.close();
		process.destroy();
		return sb.toString();
	}
}