package com.argus.file;

import java.io.File;
import java.util.List;

public class FileCrawler {

	FileCrawlerItem curItem = null;

	public FileCrawler(String initialDir) {
		curItem = new FileCrawlerItem(new File(initialDir), null);
	}

	public FileCrawler(File initialDir) {
		curItem = new FileCrawlerItem(initialDir, null);
	}

	public File getNextDir() {
		if ((curItem = curItem.crawl()) != null) {
			return curItem.getCurDir();
		}

		return null;
	}

	public List<File> getFileList() {
		return curItem.getFileList();
	}

	public static void main(String[] args) {
		FileCrawler crawler = new FileCrawler("D:/Workspace");
		File curDir = null;
		while ((curDir = crawler.getNextDir()) != null) {
			System.out.println(curDir.getAbsolutePath());
		}

	}
}
