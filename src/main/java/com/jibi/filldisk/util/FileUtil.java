package com.jibi.filldisk.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Slf4j
public class FileUtil {

    public static Map<Long, String> FILLFILESMAP;
    public static LinkedHashSet<Long> FILLORDER4;
    public static LinkedHashSet<Long> FILLORDER10;

    static {
        FILLFILESMAP = new HashMap<Long, String>();
        FILLFILESMAP.put(1L * 1, "1B");
        FILLFILESMAP.put(1L * 10, "10B");
        FILLFILESMAP.put(1L * 100, "100B");
        FILLFILESMAP.put(1L * 1024 * 1, "1KB");
        FILLFILESMAP.put(1L * 1024 * 4, "4KB");
        FILLFILESMAP.put(1L * 1024 * 10, "10KB");
        FILLFILESMAP.put(1L * 1024 * 40, "40KB");
        FILLFILESMAP.put(1L * 1024 * 100, "100KB");
        FILLFILESMAP.put(1L * 1024 * 400, "400KB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 1, "1MB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 4, "4MB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 10, "10MB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 40, "40MB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 100, "100MB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 400, "400MB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 1024 * 1, "1GB");
        FILLFILESMAP.put(1L * 1024 * 1024 * 1024 * 4, "4GB");
        //FILLFILESMAP.put(1L * 1024 * 1024 * 1024 * 10, "10GB");
        //FILLFILESMAP.put(1L * 1024 * 1024 * 1024 * 40, "40GB");
        //FILLFILESMAP.put(1L * 1024 * 1024 * 1024 * 100, "100GB");
        //FILLFILESMAP.put(1L * 1024 * 1024 * 1024 * 400, "400GB");

        FILLORDER4 = new LinkedHashSet<>();
        //FILLORDER4.add(1L * 1024 * 1024 * 1024 * 400);
        //FILLORDER4.add(1L * 1024 * 1024 * 1024 * 40);
        FILLORDER4.add(1L * 1024 * 1024 * 1024 * 4);
        FILLORDER4.add(1L * 1024 * 1024 * 400);
        FILLORDER4.add(1L * 1024 * 1024 * 40);
        FILLORDER4.add(1L * 1024 * 1024 * 4);
        FILLORDER4.add(1L * 1024 * 400);
        FILLORDER4.add(1L * 1024 * 40);
        FILLORDER4.add(1L * 1024 * 4);

        FILLORDER10 = new LinkedHashSet<>();
        //FILLORDER10.add(1L * 1024 * 1024 * 1024 * 100);
        //FILLORDER10.add(1L * 1024 * 1024 * 1024 * 10);
        FILLORDER10.add(1L * 1024 * 1024 * 1024 * 1);
        FILLORDER10.add(1L * 1024 * 1024 * 1024 * 1);
        FILLORDER10.add(1L * 1024 * 1024 * 100);
        FILLORDER10.add(1L * 1024 * 1024 * 10);
        FILLORDER10.add(1L * 1024 * 1024 * 1);
        FILLORDER10.add(1L * 1024 * 100);
        FILLORDER10.add(1L * 1024 * 10);
        FILLORDER10.add(1L * 1024 * 1);
        FILLORDER10.add(1L * 100);
        FILLORDER10.add(1L * 10);
        FILLORDER10.add(1L * 1);
    }

    public static void createFile(final String driveDumpDir, final String fromFilename, final String toFilename) throws IOException {
        Path copied = Paths.get(driveDumpDir + "/" + toFilename);
        Path originalPath = Paths.get(driveDumpDir + "/" + fromFilename);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Created fill file {}", toFilename);
    }

}
