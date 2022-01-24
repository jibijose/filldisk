package com.jibi.filldisk.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    public static Map<Integer, String> FILLFILESMAP;

    static {
        FILLFILESMAP = new HashMap<Integer, String>();
        FILLFILESMAP.put(1, "1B");
        FILLFILESMAP.put(10, "10B");
        FILLFILESMAP.put(100, "100B");
        FILLFILESMAP.put(1024 * 1, "1KB");
        FILLFILESMAP.put(1024 * 10, "10KB");
        FILLFILESMAP.put(1024 * 100, "100KB");
        FILLFILESMAP.put(1024 * 1024 * 1, "1MB");
        FILLFILESMAP.put(1024 * 1024 * 10, "10MB");
        FILLFILESMAP.put(1024 * 1024 * 100, "100MB");
        FILLFILESMAP.put(1024 * 1024 * 1024 * 1, "1GB");
        FILLFILESMAP.put(1024 * 1024 * 1024 * 10, "10GB");
        FILLFILESMAP.put(1024 * 1024 * 1024 * 100, "100GB");
    }

    private static void createFile(final String fromFilename, final String toFilename) throws Exception {
        Path copied = Paths.get("C:\\DUMPDIR\\" + toFilename);
        Path originalPath = Paths.get("src/main/resources/" + fromFilename);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }

}
