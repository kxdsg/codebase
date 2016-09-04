package com.argus.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileCrawlerItem {

	private List<File> dirList;
	private List<File> fileList;
	private int curDirIndex;
	FileCrawlerItem parentCrawlerItem;
	private File curDir;
	private boolean returnedParentDir = false;

	public FileCrawlerItem(File initialDir, FileCrawlerItem parent) {
		init(initialDir, parent);
	}

	private void init(File initialDir, FileCrawlerItem parentCrawlerItem) {
		dirList = new ArrayList();
		fileList = new ArrayList();

		this.curDir = initialDir;

		if (initialDir.isDirectory()) {
			File[] curFiles = initialDir.listFiles();
			for (File curFile : curFiles) {
				if (curFile.isFile()) {
					fileList.add(curFile);
				} else {
					dirList.add(curFile);
				}
			}
			this.parentCrawlerItem = parentCrawlerItem;
			curDirIndex = 0;
		} else {
			System.err.println("[" + initialDir + "] is not directory");
		}
	}

	public FileCrawlerItem crawl() {
		if ((curDirIndex + 1) > dirList.size()) {
			if (parentCrawlerItem != null) {
				return parentCrawlerItem.crawl();
			} else {
				if (returnedParentDir) {
					return null;
				} else {
					returnedParentDir = true;
					return this;
				}
			}
		}
		File newDir = dirList.get(curDirIndex);
		curDirIndex++;
		return new FileCrawlerItem(newDir, this);
	}

	public File getCurDir() {
		return curDir;
	}

	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}

}
