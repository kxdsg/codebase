package com.argus.file;

import java.io.File;
import java.util.List;

public class SearchDir {

	public static void searchDir (String dir, String pattern) {
		FileCrawler crawler = new FileCrawler (dir);
		File curDir = null;
		pattern = pattern.toLowerCase();
		while ((curDir = crawler.getNextDir()) != null) {
			List<File> fileList = crawler.getFileList();
			for (File curFile : fileList) {
				if (curFile.getAbsolutePath().toLowerCase().indexOf(pattern) != -1) {
					System.out.println ("Matched " + curFile.getAbsolutePath());
				}
			}
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		searchDir ("D:/work/eclipseWork/eclipseProj/SaaComparison/lib", "bcpro");
	}

}
