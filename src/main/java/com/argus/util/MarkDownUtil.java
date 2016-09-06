package com.argus.util;

import java.io.*;

public class MarkDownUtil {
	private final static String prefix = "*	";
	private final static String sep = " | ";
	private final static String tab = "\t";

	public static void main(String[] args) {
		//文件字段以tab分隔
		convert("d:\\test\\text.txt");
	}

	/**
	 * 转换成markdown格式
	 * @param filePath
	 */
	public static void convert(String filePath) {
		BufferedReader br = null;
		String lineFeed = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
			while ((lineFeed = br.readLine()) != null) {
				String[] a = lineFeed.split(tab);
				System.out.println(prefix + a[0] + sep + a[1] + sep + a[2]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				br=null;
			}
		}
	}

}
