package com.argus.file;

import com.argus.util.ExecUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * the class utility
 * 1.expand ear/war/jar file
 * 2.compare two archive file, remove same content before use professional compare tool
 */

public class ExpandJar {

	static String expandDir = "D:/Workspace/compare/temp/expand";
	
	static String decompilerProgramPath = "D:\\Tools\\decompiler\\jad.exe ";
	
	static String compareRoot1 = "D:/Workspace/compare/version1";
	static String compareRoot2 = "D:/Workspace/compare/version2";
	
	public static long counter = 0;

	public static boolean expandJar (String archiveName) {
		return expandJar (new File (archiveName));
	}


	public static void expandAll (String dirName) {
		expandAll (new File (dirName));
	}

	public static void expandAll (File dirName) {
		File[] fileList = dirName.listFiles();
		for (File curFile : fileList) {
			if (!curFile.isFile()) {
				continue;
			}
			if(isArchive(curFile.getAbsolutePath())){
				expandJar (curFile.getAbsolutePath());
			}
		}
	}

	public static boolean expandJar (File archiveName) {
		System.out.println("Expanding " + archiveName.getAbsolutePath());

		new File (expandDir).delete();
		new File (expandDir).mkdirs();

		String tempFileName = "exp_" + System.currentTimeMillis() + "_" + counter + ".zip";
		counter++;

		File tmpArchiveName = new File (expandDir + "/" + tempFileName);
		System.out.println ("  tmpFile is " + tmpArchiveName.getAbsolutePath());
		boolean fileMoved = archiveName.renameTo(tmpArchiveName);

		if (fileMoved) {
			System.out.println ("  Moved file.  Expanding now!");
			FileInputStream fis;
			archiveName.mkdirs();
			try {
				fis = new FileInputStream(tmpArchiveName);
				ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fis));
				ZipEntry entry = null;
				int BUFFER =  1024*100;
				while((entry = zin.getNextEntry()) != null) {

					byte data[] = new byte[BUFFER];
					if (entry.isDirectory()) {
						new File (archiveName.getAbsolutePath() + "/" + entry.getName()).mkdirs();
						continue;
					}
					String dirName = archiveName.getAbsolutePath() + "/" + entry.getName();
					if (dirName.lastIndexOf("/") != -1) {
						dirName = dirName.substring(0, dirName.lastIndexOf("/"));
						new File (dirName).mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(archiveName.getAbsolutePath() + "/" + entry.getName());
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
					int count = 0;
					while ((count = zin.read(data, 0, BUFFER)) != -1) {
						   dest.write(data, 0, count);
					}
					dest.flush();
		            dest.close();
		            fos.close();
				}

				tmpArchiveName.delete();

				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return false;
		} else {
			System.err.println ("  unable to move");
			return false;
		}
	}

	public static boolean isArchive (String fileName) {
        if(!new File(fileName).isFile()){
            return false;
        }
		if ( (fileName.endsWith(".ear")) ||
				  (fileName.endsWith(".jar")) ||
				  (fileName.endsWith(".war")) ||
				  (fileName.endsWith(".rar")) ) {
			return true;
		}
		return false;

	}

	/*
	 * Find all zip archive, unzip them and compare the entire directory.
	 *
	 * If the files match, then delete
	 */
	public static void compare1 (File dir1, File dir2) {

		for (File curFile : dir1.listFiles()) {
			if (isArchive (curFile.getAbsolutePath())) {
				ExpandJar.expandJar(curFile.getAbsolutePath());
			}
		}

		for (File curFile : dir2.listFiles()) {
			if (isArchive (curFile.getAbsolutePath())) {
				ExpandJar.expandJar(curFile.getAbsolutePath());
			}
		}


		Map<String, File> dir1Map = new HashMap ();

		for (File curFile : dir1.listFiles()) {
			dir1Map.put(curFile.getName(), curFile);
		}
		for (File curFile : dir2.listFiles()) {
			if (dir1Map.get(curFile.getName()) != null) {

				CompareDir compareTool = new CompareDir ();

				String compareDir1 = curFile.getAbsolutePath();
				File compareDir = dir1Map.get(curFile.getName());
				String compareDir2 = compareDir.getAbsolutePath();

				System.out.println ("Going to compare the following");
				System.out.println ("  " + compareDir1);
				System.out.println ("  " + compareDir2);

				if ((curFile.isDirectory()) && (compareDir.isDirectory())) {
					System.out.println ("  Is directory. Comparing.");

					compareTool.dir1 = compareDir1;
					compareTool.dir2 = compareDir2;

					compareTool.compare();

				} else {
					System.out.println ("  Not comparing.  Is not a directory");
				}

			} else {
				System.out.println("Can't find " + curFile.getName());
			}
		}
	}

	public static void compare2 (File dir1, File dir2Base) {
		for (File curFile : dir1.listFiles()) {
            for (File archiveFile : curFile.listFiles()) {
                System.out.println (archiveFile.getAbsolutePath());
                File comparedFile = new File (dir2Base.getAbsolutePath() + "/" + curFile.getName() + "/" + archiveFile.getName());
                if (comparedFile.exists()) {
                    System.out.println(comparedFile.getAbsolutePath());

                    if (comparedFile.isFile() && archiveFile.isFile()) {
                        if (!ExpandJar.expandJar(comparedFile.getAbsolutePath())) {
                            continue;
                        }
                        if (!ExpandJar.expandJar(archiveFile.getAbsolutePath())) {
                            continue;
                        }


                        CompareDir compareTool = new CompareDir ();

                        compareTool.dir1 = comparedFile.getAbsolutePath();
                        compareTool.dir2 = archiveFile.getAbsolutePath();

                        compareTool.compare();
                    }
                }
            }
		}
	}

	
	/**
	 * Decompile java class
	 * @param dir
	 */
	
	public static void decompileClassFile (File dir) {
		FileCrawler crawler = new FileCrawler (dir.getAbsolutePath());
		File curDir = null;
		while ((curDir = crawler.getNextDir()) != null) {
			List<File> fileList = crawler.getFileList();
			for (File curFile : fileList) {
				System.out.println ("curFile " + curFile.getAbsolutePath());
				if ((curFile.isFile()) && (curFile.getAbsolutePath().endsWith(".class"))) {
					/*
					 * It's a class file.  DECOMPILE
					 */
					System.out.println ("Decompiling " + curFile.getAbsolutePath());
					ExecUtil.execCommand(FileUtil.getDirName(curFile), decompilerProgramPath + curFile.getAbsolutePath());
				}
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File dir1 = new File (compareRoot1);
		File dir2 = new File (compareRoot2);
		compare1 (dir1, dir2);
		compare2 (dir1, dir2);
//		decompileClassFile (dir1);
//		decompileClassFile (dir2);
        System.out.println("done");
	}

}
