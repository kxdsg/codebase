package com.argus.file;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.argus.util.StringUtil;
import org.apache.commons.io.FileUtils;

import com.argus.util.DateUtil;
import com.argus.util.ExecUtil;
import com.argus.util.LoggerUtil;
import com.argus.util.StringUtil;
import com.thoughtworks.xstream.XStream;

/**
 * Common File Util
 */
public class FileUtil {

	private static LoggerUtil logger = LoggerUtil.getInstance(FileUtil.class);
	private static final String TAB = "\t";
	private static final String digFileTypes = "^.+\\.(jpg|jpeg|png|log|tmp|avi|rmvb|mkv|wmv|torrent|xml|mp3|mp4)$";
	private static final String excludedFiles = "^(?!.*?(.dll|.DLL|.crx|.json|.pak|.nexe|.itxib|.db|.dat|.datx|.psg|.ksg|.fsg|.vsg)).*";
	private static final String defaultDt = "20140728 20:08:22";
	private static final boolean checkFileType = true;
	private static final String[] watchList = new String[]{"Google\\Chrome", "Microsoft\\Internet Explorer", "Mozilla\\Firefox",
		"Thunder Network", "Xunlei", "305767735", "Music", "Resources"};

	final static int BUFFER = 2048;

	/**
	 * create bufferd Reader File > FileInputStream > InputStreamReader >
	 * BufferedReader
	 *
	 * @param file
	 * @return BufferedReader
	 */
	public static BufferedReader createBuffredReader(File file) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
		} catch (FileNotFoundException e) {
			System.err.println("File [" + file.getAbsolutePath()
					+ "] cannot be found.");
			e.printStackTrace();
			System.exit(-1);
		}
		return br;
	}

	public static BufferedReader createBuffredReader(String fileName) {
		return createBuffredReader(new File(fileName));
	}

	/**
	 * read file with BufferedReader line by line
	 *
	 * @param file
	 * @return String
	 */
	public static String readFile(File file) {
		BufferedReader br = null;
		StringBuffer buf = new StringBuffer();
		String lineFeed = null;
		boolean firstLine = true;
		br = createBuffredReader(file);
		try {
			while ((lineFeed = br.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
				} else {
					buf.append("\n");
				}
				buf.append(lineFeed);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return buf.toString();
	}

	public static String readFile(String fileName) {
		return readFile(new File(fileName));
	}

	/**
	 * read file with BufferedInputStream
	 *
	 * @param filePath
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFileWithBufferedInputStream(String filePath)
			throws IOException {

		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);

			File file = new File(filePath);
			long fileSize = file.length();
			byte[] result = new byte[(int) fileSize];
			int bytesRead = 0;
			int offset = 0;
			while (offset < fileSize) {
				bytesRead = bis.read(result, offset, (int) (fileSize - offset));
				offset = offset + bytesRead;
			}
			return result;
		} catch (IOException e) {
			throw e;
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * read file with FileInputStream
	 *
	 * @param fileName
	 * @return
	 */
	public static String readFileWithInputStream(String fileName) {
		String s = null;
		FileInputStream fis = null;
		try {
			File file = new File(fileName);
			fis = new FileInputStream(file);
			int n;
			while ((n = fis.available()) > 0) {
				byte[] b = new byte[n];
				int result = fis.read(b);
				if (result == -1)
					break;
				s = new String(b);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return s;
	}

	/**
	 * read file with FileReader
	 *
	 * @param fileName
	 * @return
	 */
	public static String readFileWithReader(String fileName) {
		FileReader fr = null;
		BufferedReader br = null;
		StringWriter writer = new StringWriter();
		try {

			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			char[] buffer = new char[1024];
			int n = 0;
			while ((n = br.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return writer.toString();

	}

	/**
	 * write string to file, use FileOutputStream write method
	 *
	 * @param fileName
	 * @param fileContent
	 */
	public static void writeFileWithFileOutputStream(String fileName,
			String fileContent) {
		FileOutputStream fos = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			fos.write(fileContent.getBytes());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * write the content of the String into the file. File will be overwritten.
	 * use PrintStream print method
	 *
	 * @param fileName
	 * @param fileContent
	 */
	public static void writeFileWithPrintStream(String fileName,
			String fileContent) {
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(fileName));
			ps.print(fileContent);
			ps.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * write file with FWriter
	 *
	 * @param content
	 * @param destFile
	 */
	public static void writeFileWithWriter(String content, File destFile) {

		BufferedWriter bufferedWriter = null;

		try {
			FileWriter fileWriter = new FileWriter(destFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String getDirName(File file) {
		if (file.isFile()) {
			String fileName = file.getAbsolutePath();
			int lastSep = fileName.lastIndexOf(File.separator);
			return fileName.substring(0, lastSep);
		} else {
			return file.getAbsolutePath();
		}
	}

	public static void printURLAccessContent(String url) {
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			StringUtil.copy(in, System.out);
			System.out.println();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static File[] listRootLister() {
		return File.listRoots();
	}

	public static void safeCopyFile(File inFile, File outFile) {

		try {
			if (inFile.getCanonicalPath().equals(outFile.getCanonicalPath())) {
				return;
			}
			FileInputStream fis = new FileInputStream(inFile);
			FileOutputStream fos = new FileOutputStream(outFile);
			StringUtil.copy(fis, fos);
			fis.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void safeCreateFile(File file) throws IOException {
		if (file.exists()) {
			throw new IOException(file.getName() + "is already exist.");
		}

		FileOutputStream fos = new FileOutputStream(file);
		fos.close();
	}

	public static void renameFile(String orginal, String dest) {
		File src = new File(orginal);
		File des = new File(dest);
		src.renameTo(des);
	}

	public static File chooseFile() {
		File f = null;
		FileDialog fd = new FileDialog(new Frame(), "Please select a file:",
				FileDialog.LOAD);
		fd.setVisible(true);
		String direc = fd.getDirectory();
		String file = fd.getFile();
		if (direc == null || file == null) {
			System.out.println("User cancel select file!");
		} else {
			f = new File(direc, file);
		}
		return f;
	}

	public static String captureCmdScreenContent(String cmd) {
		Runtime rt = Runtime.getRuntime();
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = null;
		try {
			Process pro = rt.exec(cmd);
			br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();

	}

	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile()) {
				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	public static boolean deleteDirectory(String dir) {
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * delete the special hidden directory recursively e.g. delete the .svn
	 * directory
	 *
	 * @param path
	 * @param name
	 */
	public static void recursiveDelete(String path, String name) {
		try {
			File file = new File(path);
			if (file.isDirectory()) {
				for (File item : file.listFiles()) {
					if (item.isDirectory() && name.equals(item.getName())) {
						FileUtils.deleteDirectory(item);
					} else {
						recursiveDelete(item.getAbsolutePath(), name);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Decompress a zip file using ZipInputStream
	 *
	 * @param zipFileName
	 */
	public static void unzipWithZipInputStream(String zipFileName) {
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry entry;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			fis = new FileInputStream(zipFileName);
			zis = new ZipInputStream(new BufferedInputStream(fis));
			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting:" + entry.getName());
				int count;
				byte[] data = new byte[BUFFER];
				fos = new FileOutputStream(entry.getName());
				bos = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Decompress a zip file using ZipFile
	 *
	 * @param zipFileName
	 */
	public static void unzipWithZipFile(String zipFileName) {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		ZipEntry entry;
		FileOutputStream fos = null;
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration e = zipFile.entries();
			while (e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				System.out.println("Extracting:" + entry);
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				int count;
				byte[] data = new byte[BUFFER];
				fos = new FileOutputStream(entry.getName());
				bos = new BufferedOutputStream(fos, BUFFER);
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static void zip(String fileName) {
		BufferedInputStream origin = null;
		FileOutputStream dest = null;
		ZipOutputStream zos = null;
		FileInputStream fis = null;
		ZipEntry entry;
		try {
			dest = new FileOutputStream(fileName);
			zos = new ZipOutputStream(dest);
			byte[] data = new byte[BUFFER];
			File f = new File(".");
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {

				if (files[i].isFile() && !fileName.equals(files[i].getName())) {
					System.out.println("Compress:" + files[i]);
					fis = new FileInputStream(files[i]);
					origin = new BufferedInputStream(fis, BUFFER);
					entry = new ZipEntry(files[i].getName());
					zos.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						zos.write(data, 0, count);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (origin != null) {
				try {
					origin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dest != null) {
				try {
					dest.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String appendDependency(String name, File file,
			String projectPath) {
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("(\\d+[.])+\\d+$");

		name = name.substring(0, name.length() - 4);

		Matcher m = p.matcher(name);
		String groupId = "";
		String version = "";
		boolean b = m.find();
		if (b) {
			version = m.group();
			groupId = name.substring(0, name.length() - version.length());
			if (groupId.endsWith("-")) {
				groupId = groupId.substring(0, groupId.length() - 1);
			}
		} else {
			groupId = name;
			version = "1.0.0";
		}
		String systemPath = file.getAbsolutePath().replaceAll(
				Pattern.quote("\\"), "/");
		systemPath = systemPath.replaceAll(projectPath, "\\${basedir}");

		sb.append("<dependency>\n" + "          <groupId>")
				.append(groupId)
				.append("</groupId>\n" + "          <artifactId>")
				.append(groupId)
				.append("</artifactId>\n" + "          <version>")
				.append(version)
				.append("</version>\n" + "          <scope>system</scope>\n"
						+ "          <systemPath>" + systemPath
						+ "</systemPath>\n" + "      </dependency>");
		sb.append("\n");
		return sb.toString();

	}

	public static String generateDependency(String path, String projectPath) {
		StringBuffer sb = new StringBuffer();

		File file = new File(path);
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				if (item.isFile() && item.getName().indexOf(".jar") > 0) {
					sb.append(appendDependency(item.getName(), item,
							projectPath));
				} else {
					if (item.isDirectory()) {
						sb.append(generateDependency(item.getAbsolutePath(),
								projectPath));
					}

				}
			}
		}
		return sb.toString();
	}

	public static void listFilesAndFolders(String folder) {
		File file = new File(folder);
		try {
			if (!file.exists() || !file.isDirectory()) {
				System.out.println("Parameter is not a directory");
				System.exit(1);
			}
			File[] fileArray = file.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				if (fileArray[i].isDirectory()) {
					System.out.println(fileArray[i].getAbsolutePath());
					listFilesAndFolders(fileArray[i].getAbsolutePath());
				} else {
					System.out.println(fileArray[i].getAbsolutePath());
				}
			}
		} catch (Exception e) {
			System.err.println("Unable to access the folder ");
		}
	}

	public static List listFilesAndFoldersAndSaveToList(String folder) {
		List list = new ArrayList();
		File file = new File(folder);
		try {
			if (!file.exists() || !file.isDirectory()) {
				System.out.println("Parameter is not a directory");
				System.exit(1);
			}
			File[] fileArray = file.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				if (fileArray[i].isDirectory()) {
					List subList = listFilesAndFoldersAndSaveToList(fileArray[i]
							.getAbsolutePath());
					list.addAll(subList);
				} else {
					list.add(fileArray[i].getName());
				}
			}
		} catch (Exception e) {
			System.err.println("Unable to access the folder ");
		}

		return list;
	}

	public static List convertFileContentToList(String fileName, boolean trim) {
		BufferedReader br = createBuffredReader(fileName);
		String lineFeed = null;
		List list = new ArrayList();
		String s = null;
		try {
			while ((lineFeed = br.readLine()) != null) {
				// construct content
				String[] line = lineFeed.split(",");
				String codeValue = line[11].substring(1, line[11].length() - 1);
				if (trim) {
					codeValue = codeValue.trim();
				}
				s = codeValue + line[13];
				list.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public static String getFileName(String fileLocation) {
		fileLocation = fileLocation.replaceAll("/", "\\\\");
		int lastSplit = fileLocation.lastIndexOf("\\");
		String fileName = null;
		if (lastSplit == -1)
			fileName = fileLocation;
		else {
			fileName = fileLocation.substring(lastSplit + 1);
		}

		int lastDot = fileName.lastIndexOf(".");
		if (lastDot == -1)
			return fileName;

		String extension = fileName.substring(0, lastDot);
		return extension;
	}

	public static String stripDir(String fileLocation, String topDir) {
		return fileLocation.substring(topDir.length());
	}

	public static String getFileExtension(String fileLocation) {
		fileLocation = fileLocation.replaceAll("/", "\\\\");
		int lastSplit = fileLocation.lastIndexOf("\\");
		String fileName = null;
		if (lastSplit == -1)
			fileName = fileLocation;
		else {
			fileName = fileLocation.substring(lastSplit + 1);
		}

		int lastDot = fileName.lastIndexOf(".");
		if (lastDot == -1) {
			return "";
		}
		String extension = fileName.substring(lastDot + 1);
		return extension;
	}

	public static String getFileDir(String fileLocation) {
		fileLocation = fileLocation.replaceAll("/", "\\\\");
		int lastSplit = fileLocation.lastIndexOf("\\");
		if (lastSplit == -1) {
			return "";
		}
		String dirPath = fileLocation.substring(0, lastSplit + 1);
		return dirPath;
	}

	public static void saveProp(String propFileLocation, Properties prop) {
		try {
			prop.store(new FileOutputStream(propFileLocation), "Created on "
					+ new Date());
		} catch (FileNotFoundException e) {
			logger.error(e, new Object[] { "prop file [", propFileLocation,
					"] cannot be found" });
		} catch (IOException e) {
			logger.error(e, new Object[] { "prop file [", propFileLocation,
					"] cannot be saved" });
		}
	}

	public static Properties loadProp(String propFileLocation) {
		FileInputStream fis = null;
		Properties prop = new Properties();
		try {
			fis = new FileInputStream(propFileLocation);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			logger.error(e, new Object[] { "prop file [", propFileLocation,
					"] cannot be found" });

		} catch (IOException e) {
			logger.error(e, new Object[] { " Error loading prop file [",
					propFileLocation, "]" });

		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					logger.error(e, new Object[] {
							" Error closing stream for prop file [",
							propFileLocation, "]" });
				}
		}
		return prop;
	}

	public static void saveFile(String filePath, byte[] fileContent)
			throws IOException {
		logger.debug(new Object[] { "Saving File[", filePath, "]" });
		File file = new File(filePath);
		File dirFile = new File(getFileDir(file.getAbsolutePath()));
		if (!(dirFile.exists()))
			dirFile.mkdirs();

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(fileContent);
			fos.flush();
		} finally {
			if (fos != null)
				fos.close();
		}

		logger.debug(new Object[] { "Saved File[", filePath, "]",
				Integer.valueOf(fileContent.length) });
	}

	public static String delFile(String fileLocation) {
		String removeDirCommand = "del /S /Q  \"" + fileLocation + "\"";
		try {
			saveFile("c:\\temp\\rmFile.cmd", removeDirCommand.getBytes());
			ExecUtil.execCommand("c:\\temp\\rmFile.cmd");
			return "";
		} catch (IOException e) {
			logger.error(new Object[] {
					"Unable to save delete command file to execute the following command [",
					removeDirCommand, "]" });
		}
		return "Unable to save delete command file to execute the following command ["
				+ removeDirCommand + "]";
	}

	public static String delDir(String dirLocation) {
		dirLocation = dirLocation.replaceAll("/", "\\\\");
		if (!(dirLocation.endsWith("\\")))
			dirLocation = dirLocation + "\\";

		String removeDirCommand = "del /S /Q  \"" + dirLocation + "*\"";
		removeDirCommand = removeDirCommand + "\nrmdir /S /Q \"" + dirLocation
				+ "\"";
		try {
			saveFile("c:\\temp\\rmDir.cmd", removeDirCommand.getBytes());
			int returnCode = ExecUtil.execCommand("c:\\temp\\rmDir.cmd");
			if (returnCode != 0)
				return "Error Return code " + returnCode;

			return "";
		} catch (IOException e) {
			logger.error(new Object[] {
					"Unable to save delete command file to execute the following command [",
					removeDirCommand, "]" });
		}
		return "Unable to save delete command file to execute the following command ["
				+ removeDirCommand + "]";
	}

	public static String copyFile(String srcFile, String destFile) {
		String str1;
		String filePath = getFileDir(destFile);
		File fileDir = new File(filePath);
		fileDir.mkdirs();

		byte[] buf = new byte[10240];
		int count = 0;

		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(srcFile));
			fos = new FileOutputStream(destFile);
			while ((count = bis.read(buf)) != -1) {
				fos.write(buf, 0, count);
			}

			return "";
		} catch (FileNotFoundException e) {
			return e + "[" + srcFile + "] srcFile is missing";
		} catch (IOException e) {
			logger.error(e, new Object[] { "[", srcFile,
					"] Error reading src File" });
			return e + "[" + destFile + "] Error closing output stream";
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					logger.error(e, new Object[] { "[", destFile,
							"] Error closing output stream" });
				}

			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					logger.error(e, new Object[] { "[", srcFile,
							"] Error closing src stream " });
				}
		}
	}

	public static byte[] readFileWithStringParam(String filePath)
			throws IOException {
		logger.debug(new Object[] { "Opening File[", filePath, "]" });
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			byte[] arrayOfByte1;
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);

			File file = new File(filePath);
			long fileSize = file.length();
			logger.debug(new Object[] { "File size", Long.valueOf(fileSize) });
			byte[] result = new byte[(int) fileSize];
			int bytesRead = 0;
			int offset = 0;
			while (offset < fileSize) {
				bytesRead = bis.read(result, offset, (int) (fileSize - offset));
				offset += bytesRead;
			}
			return result;
		} catch (IOException e) {
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String readFileAsString(File file) throws IOException {
		return readFileAsString(file.getAbsolutePath());
	}

	public static String readFileAsString(String filePath) throws IOException {
		BufferedReader br = null;
		StringBuffer buf = new StringBuffer();
		String lineFeed = null;
		boolean firstLine = true;
		br = createBuffredReader(filePath);
		if (br == null) {
			return null;
		}

		try {
			while ((lineFeed = br.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
				} else {
					buf.append("\n");
				}
				buf.append(lineFeed);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buf.toString();
	}

	public static PrintStream createPrintStream(String filePath) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream outp = new PrintStream(fos);
		return outp;
	}

	public static String readLastLine(File file) throws IOException {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			long len = raf.length();
			if (len == 0L) {
				return "";
			} else {
				long pos = len - 1;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						break;
					}
				}
				if (pos == 0) {
					raf.seek(0);
				}
				byte[] bytes = new byte[(int) (len - pos)];
				raf.read(bytes);
				return new String(bytes);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				raf.close();
			}
		}

		return null;

	}
	
	/**
	 * 
	 * @param dir
	 * @param interval
	 */
	public static void findLatestModifiedFiles(String dir, int interval){
		Calendar cal = Calendar.getInstance(); 
		try {
			File directory = new File(dir);
			if(directory.canRead()){
			    File[] files = directory.listFiles();
			    if(files != null){
			    	for (File item : files) {
						if (item.isFile()) {
							long time = item.lastModified();
							cal.setTimeInMillis(time);
							Date date = null;
							if(interval == 0){
								date = DateUtil.parseDate(defaultDt, DateUtil.YYYYMMDDHH24MMSS);
							} else {
								date = DateUtil.getIntervalDate(new Date(), interval);
							}
							
							if(time > date.getTime()){
								if(isWatch(item.getAbsolutePath())){
									if(checkFileType){
										if(match(excludedFiles, item.getAbsolutePath())){
											logger.info( DateUtil.formatDate(cal.getTime(), DateUtil.YYYYMMDDHH24MMSS) + TAB + item.getAbsolutePath());
										}
									} else {
										logger.info( DateUtil.formatDate(cal.getTime(), DateUtil.YYYYMMDDHH24MMSS) + TAB + item.getAbsolutePath());
									}
								} else {
									//white list, no need check.
								}
								
							}
						} else {
							findLatestModifiedFiles(item.getAbsolutePath(), interval);
						}
					}
			    } else {
			    	//
			    }
				
			} else {
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			
		}
		
		
		
	}	
	
	
	public static boolean match(String pattern, String str){
		boolean match = false;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		if(m.matches()){
			match = true;
		};
		return match;
	}
	
	public static boolean isWatch(String path){
		boolean isWatch = false;
		for(String item: watchList){
			if(path.toUpperCase().contains(item.toUpperCase())){
				isWatch = true;
				break;
			}
		}
		return isWatch;
	}

	public static void main(String[] args) throws Exception {


//		findLatestModifiedFiles("D:\\", 0);
//		System.out.println("process done");
		
//		FileUtil.detectFiles("D:\\workspace");

//		PrintWriter pw = new PrintWriter(new File("d:\\test\\hello.txt"));
//		for(int i = 0;i<100000;i++){
//			pw.write("hello, i am test message " + i + "\n");
//		}
//		pw.flush();
//		pw.close();

		File file = new File("/Users/xingding/Downloads/abc.txt");
		System.out.println(file.length());
		System.out.println("Byte: " + Byte.SIZE);
		System.out.println("Short: " + Short.SIZE);
		System.out.println("Character: " + Character.SIZE);
		System.out.println("Integer: " + Integer.SIZE);
		System.out.println("Float: " + Float.SIZE);
		System.out.println("Long: " + Long.SIZE);
		System.out.println("Double: " + Double.SIZE);
		System.out.println("Boolean: " + Boolean.toString(false));

	}
	
	interface FileFilter{
		public void doFilter();
	}
	
	public static void process(File file, FileFilter filter){
		filter.doFilter();
	}
	
	
	public static void detectFiles(String filePath){
		if(filePath == null || filePath.length() == 0){
			logger.error("invalid file path!");
			return;
		}
		File dir = new File(filePath);
		if(!dir.exists()){
			logger.error("failed to find the target directory!");
			return;
		}
		if(!dir.isDirectory()){
			logger.error("target object is not directory!");
			return;
		}
		File[] files = dir.listFiles();
		for(final File f : files){
			if(f.isDirectory()){
//				logger.info("directory:" + f.getAbsolutePath());
				detectFiles(f.getAbsolutePath());
			} else {
				process(f, new FileFilter(){

					@Override
					public void doFilter() {
						if(f.getName().endsWith(".jpg")){
							logger.info(f.getAbsolutePath());
						}
						
					}
					
				});
			}
			
		}
		
		
		
	}

}
