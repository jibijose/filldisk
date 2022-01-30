package com.jibi.filldisk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StopWatch;

import java.io.File;
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

    public static long BYTES1GB = 1024 * 1024 * 1024;
    public static long BYTES1MB = 1024 * 1024;
    public static long BYTES1KB = 1024;
    public static long BYTES1B = 1;

    public static long BYTES4GB = BYTES1GB * 4;
    public static long BYTES4MB = BYTES1MB * 4;
    public static long BYTES4KB = BYTES1KB * 4;

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

        FILLORDER4 = new LinkedHashSet<>();
        FILLORDER4.add(1L * 1024 * 1024 * 1024 * 4);
        FILLORDER4.add(1L * 1024 * 1024 * 400);
        FILLORDER4.add(1L * 1024 * 1024 * 40);
        FILLORDER4.add(1L * 1024 * 1024 * 4);
        FILLORDER4.add(1L * 1024 * 400);
        FILLORDER4.add(1L * 1024 * 40);
        FILLORDER4.add(1L * 1024 * 4);

        FILLORDER10 = new LinkedHashSet<>();
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

    public static void createFileStatic(final String driveDumpDir, final int iFile, final String toFilename) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String toCopyFileName = DateUtil.getDateTimeFormatted() + "-" + toFilename;
        log.debug("Creating random {} file {}", iFile, toFilename);
        try {
            Path copied = Paths.get(driveDumpDir, toCopyFileName);
            Path originalPath = Paths.get(driveDumpDir, toFilename);
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
            stopWatch.stop();
            log.info("Created fill {} file {} in {} seconds", iFile, toCopyFileName, stopWatch.getTotalTimeSeconds());
        } catch (IOException ioException) {
            stopWatch.stop();
            log.warn("Exception creating fill {} file {} in {} seconds", iFile, toCopyFileName, stopWatch.getTotalTimeSeconds(), ioException);
        }
    }

    public static void createFileRandom(final String driveDumpDir, final int iFile, final String toFilename, long byteSize) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String toCopyFileName = DateUtil.getDateTimeFormatted() + "-" + toFilename;
        log.debug("Creating random {} file {} with size {}", iFile, toFilename, (int) byteSize);
        try {
            String fileContent = RandomStringUtils.randomAlphanumeric((int) byteSize);
            File file = new File(driveDumpDir + "/" + toCopyFileName);
            FileUtils.writeStringToFile(file, fileContent, "UTF-8", false);
            stopWatch.stop();
            log.info("Created random {} file {} with size {} in {} seconds", iFile, toFilename, (int) byteSize, stopWatch.getTotalTimeSeconds());
        } catch (IOException ioException) {
            stopWatch.stop();
            log.warn("Exception creating random {} file {} with size {} in {} seconds", iFile, toCopyFileName, (int) byteSize, stopWatch.getTotalTimeSeconds(), ioException);
        }
    }

}
