package com.argus.file;

import java.io.File;

public class ZipUtil {

    public static void expandArchive(File file) {
        boolean fileMoved = file.renameTo(new File(file.getAbsolutePath() + ".tmp"));
        if (!fileMoved) {

        }
    }

    public static boolean isExistZip(String directory, String filename) {
        return true;
    }

    public static void unzip(String path, String tempDir, String fileName) {
        //todo
    }

    public static void unzip(String fullPath, String tempDir) {
        //todo
    }

}
