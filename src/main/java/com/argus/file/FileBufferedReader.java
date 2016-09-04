package com.argus.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileBufferedReader {

	private BufferedReader br = null;
	private String lineFeed = null;

	public FileBufferedReader (File file) {
		init (file);
	}

	public FileBufferedReader (String fileName) {
		init(new File (fileName));
	}

	private void init (File file) {
		br = FileUtil.createBuffredReader(file);
	}

	/**
	 * method to indicate whether there's a next line.  If there is then calling the getLine command will provide the next line
	 * @return
	 */
	public boolean hasNext () {
		try {
			lineFeed = br.readLine();

			if (lineFeed != null) {
				return true;
			}
		} catch (IOException e) {
			lineFeed = null;
			e.printStackTrace();
		}
		return false;
	}

	public String getLine () {
		return lineFeed;
	}

	public void close () {

	}

}
