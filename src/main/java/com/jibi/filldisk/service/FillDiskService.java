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
import java.util.Iterator;

@Service
@Slf4j
public class FillDiskService {

    public void fillDriveRandom(String driveLetter, int fillSize) throws IOException {
        if (driveLetter == null || fillSize == -1) {
            log.info("Null values passed");
            return;
        }

        SystemUtil.getDrivesInfo();

        File fileDrive = getDrive(driveLetter);
        String driveDumpDir = getDriveDumpDir(driveLetter);

        FileUtils.deleteDirectory(new File(driveDumpDir));
        File fileDriveDumppDir = new File(driveDumpDir);
        fileDriveDumppDir.mkdirs();
        log.debug("Created directory {}", driveDumpDir);

        if (fillSize == 4) {
            startDumpRandomFiles4(fileDrive, driveDumpDir);
        } else if (fillSize == 10) {
            startDumpRandomFiles10(fileDrive, driveDumpDir);
        }
    }

    public void fillDriveStatic(String driveLetter, int fillSize) throws IOException {
        if (driveLetter == null || fillSize == -1) {
            log.info("Null values passed");
            return;
        }

        SystemUtil.getDrivesInfo();

        File fileDrive = getDrive(driveLetter);
        String driveDumpDir = getDriveDumpDir(driveLetter);

        FileUtils.deleteDirectory(new File(driveDumpDir));
        File fileDriveDumppDir = new File(driveDumpDir);
        fileDriveDumppDir.mkdirs();
        log.debug("Created directory {}", driveDumpDir);

        createFillFiles(fileDriveDumppDir, fillSize);
        log.info("Completed creating fill template files");

        if (fillSize == 4) {
            startDumpStaticFiles4(fileDrive, driveDumpDir);
        } else if (fillSize == 10) {
            startDumpStaticFiles10(fileDrive, driveDumpDir);
        }
    }

    private File getDrive(String driveLetter) {
        if (SystemUtil.isWindowsOS()) {
            String drive = driveLetter + ":\\";
            log.info("Windows drive = {}", drive);
            return new File(drive);
        } else {
            String drive = driveLetter;
            log.info("Non-Windows drive = {}", drive);
            return new File(drive);
        }
    }

    private String getDriveDumpDir(String driveLetter) {
        String driveDumpDir;
        if (SystemUtil.isWindowsOS()) {
            String drive = driveLetter + ":\\";
            driveDumpDir = drive + "\\DUMPDIR";
            log.info("Windows drive dump dir = {}", driveDumpDir);
        } else {
            String drive = driveLetter;
            if (drive.endsWith("/")) {
                driveDumpDir = drive + "DUMPDIR";
            } else {
                driveDumpDir = drive + "/DUMPDIR";
            }
            log.info("Non-Windows drive dump dir = {}", driveDumpDir);
        }
        return driveDumpDir;
    }

    private boolean createFillFiles(File fileDriveDumppDir, int fillSize) throws IOException {
        if (SystemUtil.getFreeSpace(fileDriveDumppDir) == 0) {
            return false;
        }

        if (fillSize == 4) {
            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 4);
            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 40);
            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 400);

            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 1024 * 4);
            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 1024 * 40);
            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 1024 * 400);

            checkAndCreateFillFile4(fileDriveDumppDir, 1L * 1024 * 1024 * 1024 * 4);
        } else if (fillSize == 10) {
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1);
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 10);
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 100);

            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 1);
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 10);
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 100);

            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 1024 * 1);
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 1024 * 10);
            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 1024 * 100);

            checkAndCreateFillFile10(fileDriveDumppDir, 1L * 1024 * 1024 * 1024 * 1);
        }
        return true;
    }

    private void checkAndCreateFillFile4(File fileDriveDumppDir, long sizeBytes) throws IOException {
        if (SystemUtil.getFreeSpace(fileDriveDumppDir) / sizeBytes > 0) {
            String fileName = fileDriveDumppDir + "/" + FileUtil.FILLFILESMAP.get(sizeBytes);
            createFillFile4(fileDriveDumppDir, new File(fileName), sizeBytes);
            log.info("Created fill template file {}", FileUtil.FILLFILESMAP.get(sizeBytes));
        } else {
            log.info("Skipped fill template file {}", FileUtil.FILLFILESMAP.get(sizeBytes));
        }
    }

    private void checkAndCreateFillFile10(File fileDriveDumppDir, long sizeBytes) throws IOException {
        if (SystemUtil.getFreeSpace(fileDriveDumppDir) / sizeBytes > 0) {
            String fileName = fileDriveDumppDir + "/" + FileUtil.FILLFILESMAP.get(sizeBytes);
            createFillFile10(fileDriveDumppDir, new File(fileName), sizeBytes);
            log.info("Created fill template file {}", FileUtil.FILLFILESMAP.get(sizeBytes));
        } else {
            log.info("Skipped fill template file {}", FileUtil.FILLFILESMAP.get(sizeBytes));
        }
    }

    private long createFillFileFromTemplateCopy(long pendingSizeBytes, File fileDriveDumpDir, File fileName, int fillSize) throws IOException {
        FileInputStream toCopyFileInputStream;
        String toCopyFileContent;
        long originalPendingSizeBytes = pendingSizeBytes;

        Iterator<Long> iterLinkedList = null;
        if (fillSize == 4) {
            iterLinkedList = FileUtil.FILLORDER4.iterator();
        } else if (fillSize == 10) {
            iterLinkedList = FileUtil.FILLORDER10.iterator();
        }

        while (iterLinkedList.hasNext()) {
            long myCopySize = iterLinkedList.next();
            while (pendingSizeBytes - myCopySize > 0 || (pendingSizeBytes == myCopySize && originalPendingSizeBytes != myCopySize)) {
                log.debug("Pending bytes = {}, copy block size = {}", pendingSizeBytes, myCopySize);
                toCopyFileInputStream = new FileInputStream(fileDriveDumpDir.getAbsoluteFile() + "/" + FileUtil.FILLFILESMAP.get(myCopySize));
                toCopyFileContent = IOUtils.toString(toCopyFileInputStream, "UTF-8");
                FileUtils.writeStringToFile(fileName, toCopyFileContent, "UTF-8", true);
                pendingSizeBytes = pendingSizeBytes - myCopySize;
            }
        }

        return pendingSizeBytes;
    }

    private void createFillFile4(File fileDriveDumpDir, File fileName, long sizeBytes) throws IOException {
        log.debug("Writing template file {} Space left = {} Write Bytes = {}", fileName, SystemUtil.getFreeSpace(fileDriveDumpDir), sizeBytes);
        long pendingSizeBytes = sizeBytes;

        log.debug("Pending bytes = {}", pendingSizeBytes);
        if (sizeBytes == 1L * 1024 * 4) {
            while (pendingSizeBytes > 0) {
                FileUtils.writeStringToFile(fileName, "0", "UTF-8", true);
                pendingSizeBytes--;
            }
        } else {
            pendingSizeBytes = createFillFileFromTemplateCopy(pendingSizeBytes, fileDriveDumpDir, fileName, 4);
        }
    }

    private void createFillFile10(File fileDriveDumpDir, File fileName, long sizeBytes) throws IOException {
        log.debug("Writing template file {} Space left = {} Write Bytes = {}", fileName, SystemUtil.getFreeSpace(fileDriveDumpDir), sizeBytes);
        long pendingSizeBytes = sizeBytes;

        log.debug("Pending bytes = {}", pendingSizeBytes);
        if (sizeBytes == 1L * 1) {
            while (pendingSizeBytes > 0) {
                FileUtils.writeStringToFile(fileName, "0", "UTF-8", true);
                pendingSizeBytes--;
            }
        } else {
            pendingSizeBytes = createFillFileFromTemplateCopy(pendingSizeBytes, fileDriveDumpDir, fileName, 10);
        }
    }

    private void startDumpRandomFiles4(File fileDrive, String driveDumpDir) throws IOException {
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 100 / 4 > 0) {
            log.debug("Trying to create fill file 400MB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-400MB", FileUtil.BYTES4MB * 100);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 10 / 4 > 0) {
            log.debug("Trying to create fill file 40MB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-40MB", FileUtil.BYTES4MB * 10);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 4 > 0) {
            log.debug("Trying to create fill file 4MB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-4MB", FileUtil.BYTES4MB * 1);
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 100 / 4 > 0) {
            log.debug("Trying to create fill file 400KB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-400KB", FileUtil.BYTES4KB * 100);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 10 / 4 > 0) {
            log.debug("Trying to create fill file 40KB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-40KB", FileUtil.BYTES4KB * 10);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 4 > 0) {
            log.debug("Trying to create fill file 4KB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-4KB", FileUtil.BYTES4KB * 1);
        }
    }

    private void startDumpRandomFiles10(File fileDrive, String driveDumpDir) throws IOException {
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 100 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-100MB", FileUtil.BYTES1MB * 100);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 10 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-10MB", FileUtil.BYTES1MB * 10);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-1MB", FileUtil.BYTES1MB * 1);
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 100 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-100KB", FileUtil.BYTES1KB * 100);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 10 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-10KB", FileUtil.BYTES1KB * 10);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-1KB", FileUtil.BYTES1KB * 1);
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 100 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-100B", FileUtil.BYTES1B * 100);
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 10 > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-10B", FileUtil.BYTES1B * 10);
        }
        while (SystemUtil.getFreeSpace(fileDrive) > 0) {
            FileUtil.createFileRandom(driveDumpDir, DateUtil.getDateTimeFormatted() + "-1B", FileUtil.BYTES1B * 1);
        }
    }

    private void startDumpStaticFiles4(File fileDrive, String driveDumpDir) throws IOException {
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 1024 / 4 > 0) {
            log.debug("Trying to create fill file 4GB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "4GB", DateUtil.getDateTimeFormatted() + "-4GB");
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 100 / 4 > 0) {
            log.debug("Trying to create fill file 400MB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "400MB", DateUtil.getDateTimeFormatted() + "-400MB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 10 / 4 > 0) {
            log.debug("Trying to create fill file 40MB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "40MB", DateUtil.getDateTimeFormatted() + "-40MB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 1024 / 4 > 0) {
            log.debug("Trying to create fill file 4MB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "4MB", DateUtil.getDateTimeFormatted() + "-4MB");
        }

        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 100 / 4 > 0) {
            log.debug("Trying to create fill file 400KB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "400KB", DateUtil.getDateTimeFormatted() + "-400KB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 10 / 4 > 0) {
            log.debug("Trying to create fill file 40KB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "40KB", DateUtil.getDateTimeFormatted() + "-40KB");
        }
        while (SystemUtil.getFreeSpace(fileDrive) / 1024 / 4 > 0) {
            log.debug("Trying to create fill file 4KB space left = [{}]", SystemUtil.getFormattedFreeSpace(fileDrive));
            FileUtil.createFile(driveDumpDir, "4KB", DateUtil.getDateTimeFormatted() + "-4KB");
        }
    }

    private void startDumpStaticFiles10(File fileDrive, String driveDumpDir) throws IOException {
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
}
