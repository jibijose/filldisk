package com.jibi.filldisk.service;

import com.jibi.filldisk.util.DateUtil;
import com.jibi.filldisk.util.FileUtil;
import com.jibi.filldisk.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@Slf4j
public class FillDiskService {

    public void fillDrive(String driveLetter) throws IOException {
        String drive = driveLetter + ":\\";
        File fileDrive = new File(drive);

        String driveDumpDir = drive + "\\DUMPDIR";
        //FileUtils.deleteDirectory(new File(driveDumpDir));
        File fileDriveDumppDir = new File(driveDumpDir);
        //fileDriveDumppDir.mkdirs();
        log.debug("Created directory {}", driveDumpDir);

        createFillFiles(fileDriveDumppDir);
        log.info("Completed creating fill template files");

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 1024 / 100 > 0) {
            FileUtil.createFile(driveDumpDir, "100GB", DateUtil.getDateTimeFormatted() + "-100GB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 1024 / 10 > 0) {
            FileUtil.createFile(driveDumpDir, "10GB", DateUtil.getDateTimeFormatted() + "-10GB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 1024 > 0) {
            FileUtil.createFile(driveDumpDir, "1GB", DateUtil.getDateTimeFormatted() + "-1GB");
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 100 > 0) {
            FileUtil.createFile(driveDumpDir, "100MB", DateUtil.getDateTimeFormatted() + "-100MB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 10 > 0) {
            FileUtil.createFile(driveDumpDir, "10MB", DateUtil.getDateTimeFormatted() + "-10MB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 > 0) {
            FileUtil.createFile(driveDumpDir, "1MB", DateUtil.getDateTimeFormatted() + "-1MB");
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 100 > 0) {
            FileUtil.createFile(driveDumpDir, "100KB", DateUtil.getDateTimeFormatted() + "-100KB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 10 > 0) {
            FileUtil.createFile(driveDumpDir, "10KB", DateUtil.getDateTimeFormatted() + "-10KB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 > 0) {
            FileUtil.createFile(driveDumpDir, "1KB", DateUtil.getDateTimeFormatted() + "-1KB");
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 100 > 0) {
            FileUtil.createFile(driveDumpDir, "100B", DateUtil.getDateTimeFormatted() + "-100B");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 10 > 0) {
            FileUtil.createFile(driveDumpDir, "10B", DateUtil.getDateTimeFormatted() + "-10B");
        }
        while (SystemUtil.getFreeSpace(fileDrive) > 0) {
            FileUtil.createFile(driveDumpDir, "1B", DateUtil.getDateTimeFormatted() + "-1B");
        }
    }

    private boolean createFillFiles(File fileDriveDumppDir) throws IOException {
        if (SystemUtil.getFreeSpace(fileDriveDumppDir) == 0) {
            return false;
        }

        /*checkAndCreateFillFile(fileDriveDumppDir, 1);
        checkAndCreateFillFile(fileDriveDumppDir, 10);
        checkAndCreateFillFile(fileDriveDumppDir, 100);

        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1);
        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 10);
        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 100);

        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1024 * 1);
        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1024 * 10);
        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1024 * 100);

        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1024 * 1024 * 1);*/
        checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1024 * 1024 * 10);
        //checkAndCreateFillFile(fileDriveDumppDir, 1024 * 1024 * 1024 * 100);

        return true;
    }

    private void checkAndCreateFillFile(File fileDriveDumppDir, int sizeBytes) throws IOException {
        if (SystemUtil.getFreeSpace(fileDriveDumppDir) / sizeBytes > 0) {
            String fileName = fileDriveDumppDir + "\\" + FileUtil.FILLFILESMAP.get(sizeBytes);
            createFillFile(fileDriveDumppDir, new File(fileName), sizeBytes);
            log.info("Created fill template file {}", FileUtil.FILLFILESMAP.get(sizeBytes));
        } else {
            log.info("Skipped fill template file {}", FileUtil.FILLFILESMAP.get(sizeBytes));
        }
    }

    private void createFillFile(File fileDriveDumppDir, File fileName, int sizeBytes) throws IOException {
        log.debug("Writing template file {}", fileName);
        int pendingSizeBytes = sizeBytes;
        int toWritePendingSizeBytes;
        int toCopySize;
        FileInputStream toCopyFileInputStream;
        String toCopyFileContent;

        toWritePendingSizeBytes = pendingSizeBytes % 1024;
        while (toWritePendingSizeBytes > 0) {
            FileUtils.writeStringToFile(fileName, "0", "UTF-8", true);
            toWritePendingSizeBytes--;
            pendingSizeBytes--;
        }

        if (pendingSizeBytes == 1024) {
            toCopySize = 1;
            toCopyFileInputStream = new FileInputStream(fileDriveDumppDir.getAbsoluteFile() + "\\" + FileUtil.FILLFILESMAP.get(toCopySize));
            toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
            while (pendingSizeBytes > 0) {
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - toCopySize;
            }
        }

        if (pendingSizeBytes > 0) {
            toCopySize = 1024;
            toCopyFileInputStream = new FileInputStream(fileDriveDumppDir.getAbsoluteFile() + "\\" + FileUtil.FILLFILESMAP.get(toCopySize));
            toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
            while (pendingSizeBytes / toCopySize > 0) {
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - toCopySize;
            }
        }

        if (pendingSizeBytes == 1024 * 1024) {
            toCopySize = 1024;
            toCopyFileInputStream = new FileInputStream(fileDriveDumppDir.getAbsoluteFile() + "\\" + FileUtil.FILLFILESMAP.get(toCopySize));
            toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
            while (pendingSizeBytes > 0) {
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - toCopySize;
            }
        }

        if (pendingSizeBytes > 0) {
            toCopySize = 1024 * 1024;
            toCopyFileInputStream = new FileInputStream(fileDriveDumppDir.getAbsoluteFile() + "\\" + FileUtil.FILLFILESMAP.get(toCopySize));
            toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
            while (pendingSizeBytes / toCopySize > 0) {
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - toCopySize;
            }
        }

        if (pendingSizeBytes == 1024 * 1024 * 1024) {
            toCopySize = 1024 * 1024;
            toCopyFileInputStream = new FileInputStream(fileDriveDumppDir.getAbsoluteFile() + "\\" + FileUtil.FILLFILESMAP.get(toCopySize));
            toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
            while (pendingSizeBytes > 0) {
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - toCopySize;
            }
        }

        if (pendingSizeBytes > 0) {
            toCopySize = 1024 * 1024 * 1024;
            toCopyFileInputStream = new FileInputStream(fileDriveDumppDir.getAbsoluteFile() + "\\" + FileUtil.FILLFILESMAP.get(toCopySize));
            toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
            while (pendingSizeBytes / toCopySize > 0) {
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - toCopySize;
            }
        }

    }
}
