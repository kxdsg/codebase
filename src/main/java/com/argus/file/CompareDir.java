package com.argus.file;

import com.argus.crypto.CipherUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the compare util class is only applicable to directory
 * 
 * @author xingding
 * 
 */
public class CompareDir {

	public String dir1 = "";
	public String dir2 = "";

	static String compareRootDir1 = "D:/Workspace/compare/version1";
	static String compareRootDir2 = "D:/Workspace/compare/version2";

	static String match1FileDir = "D:/Workspace/compare/temp/matched1";
	static String match2FileDir = "D:/Workspace/compare/temp/matched2";

	public void compare() {

		File dir1File = new File(dir1);
		File match1File = new File(match1FileDir);
		match1File.mkdirs();

		Map<String, File> dir1Map = new HashMap();

		File dir2File = new File(dir2);
		File match2File = new File(match2FileDir);
		match2File.mkdirs();

		FileCrawler crawler = new FileCrawler(dir1File.getAbsolutePath());
		File curDir = null;
		while ((curDir = crawler.getNextDir()) != null) {
			List<File> fileList = crawler.getFileList();
			for (File curFile : fileList) {
				String curFileName = curFile.getAbsolutePath().substring(
						dir1.length() + 1);
				dir1Map.put(curFileName, curFile);
			}
		}

		/*
		 * now compare to dir2
		 */
		crawler = new FileCrawler(dir2File.getAbsolutePath());
		curDir = null;
		while ((curDir = crawler.getNextDir()) != null) {
			List<File> fileList = crawler.getFileList();
			for (File curFile : fileList) {
				String curFileName = curFile.getAbsolutePath().substring(
						dir2.length() + 1);

				File comparedFile = dir1Map.get(curFileName);
				if (comparedFile != null) {
					if (CipherUtil.calcMD5(comparedFile).equals(
							CipherUtil.calcMD5(curFile))) {
						/*
						 * they matched move to the matched dir
						 */
						System.out.println("  Files " + curFile.getName()
								+ " match");
						comparedFile.renameTo(new File(match1File
								.getAbsolutePath()
								+ "/"
								+ System.currentTimeMillis()
								+ "_"
								+ ExpandJar.counter));
						ExpandJar.counter++;
						curFile.renameTo(new File(match2File.getAbsolutePath()
								+ "/" + System.currentTimeMillis() + "_"
								+ ExpandJar.counter));
					} else {
						System.out.println("  Files " + curFile.getName()
								+ " not not match");
					}
				}

			}
		}
	}

	public static void main(String[] args) {

		File dir1 = new File(compareRootDir1);
		File dir2 = new File(compareRootDir2);

		Map<String, File> dir1Map = new HashMap();

		for (File curFile : dir1.listFiles()) {
			dir1Map.put(curFile.getName(), curFile);
		}
		for (File curFile : dir2.listFiles()) {
			if (dir1Map.get(curFile.getName()) != null) {

				CompareDir compareTool = new CompareDir();

				String compareDir1 = curFile.getAbsolutePath();
				String compareDir2 = dir1Map.get(curFile.getName())
						.getAbsolutePath();

				System.out.println("Going to compare the following");
				System.out.println("  " + compareDir1);
				System.out.println("  " + compareDir2);

				compareTool.dir1 = compareDir1;
				compareTool.dir2 = compareDir2;

				compareTool.compare();

			}

		}

	}
}
