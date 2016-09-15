package com.argus.util;

import java.io.*;
import java.util.regex.Pattern;

public class MarkDownUtil {
	private final static String prefix = "*	";
	private final static String sep = " | ";
	private final static String tab = "\t";
	private final static String crlf = "\r\n";

	public static void main(String[] args) throws Exception{
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

	/**
	 * 加上table支持
	 * @param filePath
	 * @throws Exception
     */
	public static void trick(String filePath) throws Exception {
		File file = new File(filePath);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			String lineFeed = null;
			for(File ef : files){
				String fname = ef.getName();
				System.out.println("Start to process file " + fname);
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ef)));
				FileWriter fw = new FileWriter(new File(filePath + "(new)/" + fname));
				while((lineFeed=br.readLine())!=null){
					if("*\t**名称 | 必填 |\t数据类型 | 说明**".equals(lineFeed.trim())){
						fw.write("|   名称 | 必填 |\t数据类型 | 说明 |");
						fw.write(crlf);
						fw.write("|   :---: |   :---: |   :---: | :---:   |");
					} else if("*\t**名称 | 类型 | 说明**".equals(lineFeed.trim())){
						fw.write("|   名称 |  数据类型 | 说明 |");
						fw.write(crlf);
						fw.write("|   :---: |  :---: | :---:   |");
					} else if(lineFeed.trim().startsWith("*\t")){
						fw.write(lineFeed.replace("*","|"));
					} else {
						fw.write(lineFeed);
					}
					fw.write(crlf);
				}
				fw.flush();
			}

		} else {
			System.err.println("[ERROR] The path is not directory");
		}

	}

}
