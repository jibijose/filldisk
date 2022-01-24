package com.jibi.filldisk.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    public static void createFile(final String driveDumpDir, final String fromFilename, final String toFilename) throws IOException {
        Path copied = Paths.get(driveDumpDir + "\\" + toFilename);
        Path originalPath = Paths.get(driveDumpDir + "\\" + fromFilename);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Created fill file {}", toFilename);
    }

}
