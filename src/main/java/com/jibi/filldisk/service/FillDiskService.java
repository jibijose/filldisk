package com.jibi.filldisk.service;

import com.jibi.filldisk.util.FileUtil;
import com.jibi.filldisk.util.SystemUtil;
import com.jibi.filldisk.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@Slf4j
public class FillDiskService {

    public void fillDriveRandom(String driveLetter, int fillSize, int threads) throws IOException, InterruptedException {
        if (driveLetter == null || driveLetter == "" || fillSize == 0) {
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
            startDumpRandomFiles4(fileDrive, driveDumpDir, threads);
        } else if (fillSize == 10) {
            startDumpRandomFiles10(fileDrive, driveDumpDir, threads);
        }
    }

    public void fillDriveTunedRandom(String driveLetter, int fillSize, int threads) throws IOException, InterruptedException {
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
            startDumpTunedRandomFiles4(fileDrive, driveDumpDir, threads);
        } else if (fillSize == 10) {
            startDumpTunedRandomFiles10(fileDrive, driveDumpDir, threads);
        }
    }

    public void fillDriveStatic(String driveLetter, int fillSize, int threads) throws IOException, InterruptedException {
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
            startDumpStaticFiles4(fileDrive, driveDumpDir, threads);
        } else if (fillSize == 10) {
            startDumpStaticFiles10(fileDrive, driveDumpDir, threads);
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

    private void executeRandomInThreadPool(File fileDrive, String driveDumpDir, int threads, long fileSizeBytes, String fileNameSuffix) throws InterruptedException {
        int numOfFiles = (int) (SystemUtil.getFreeSpace(fileDrive) / fileSizeBytes);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        IntStream.rangeClosed(1, numOfFiles).forEach(iFile -> {
            executor.submit(() -> {
                FileUtil.createFileRandom(driveDumpDir, iFile, fileNameSuffix, fileSizeBytes);
                return null;
            });
            Util.sleepMillisSilent(10);
        });

        while (executor.getTaskCount() != executor.getCompletedTaskCount()) {
            log.info("Execution tasks for file size {} completed/total [{}/{}]", fileNameSuffix, executor.getCompletedTaskCount(), executor.getTaskCount());
            Thread.sleep(60 * 1000);
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
    }

    private void executeTunedRandomInThreadPool(File fileDrive, String driveDumpDir, int threads, long fileSizeBytes, String fileNameSuffix) throws InterruptedException {
        AtomicInteger fileCounter = new AtomicInteger(0);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);

        int numOfFiles = (int) (SystemUtil.getFreeSpace(fileDrive) / fileSizeBytes);
        int previousNumOfFiles = Integer.MAX_VALUE;
        log.info("Number of pending files {} for free space {} and file size {}", numOfFiles, (SystemUtil.getFreeSpace(fileDrive)), fileSizeBytes);
        while (numOfFiles > 0 && previousNumOfFiles != numOfFiles) {
            previousNumOfFiles = numOfFiles;
            if (numOfFiles > threads) {
                numOfFiles = threads;
            }

            IntStream.rangeClosed(1, numOfFiles).forEach(iFile -> {
                executor.submit(() -> {
                    int fileIndex = fileCounter.incrementAndGet();
                    FileUtil.createFileTuneRandom(driveDumpDir, fileIndex, fileNameSuffix, fileSizeBytes);
                    return null;
                });
                Util.sleepMillisSilent(10);
            });

            while (executor.getTaskCount() != executor.getCompletedTaskCount()) {
                log.trace("Execution tasks for file size {} completed/total [{}/{}]", fileNameSuffix, executor.getCompletedTaskCount(), executor.getTaskCount());
                Util.sleepMillisSilent(100);
            }
            log.info("Execution tasks for file size {} completed/total [{}/{}]", fileNameSuffix, executor.getCompletedTaskCount(), executor.getTaskCount());
            numOfFiles = (int) (SystemUtil.getFreeSpace(fileDrive) / fileSizeBytes);
            log.info("Number of pending files {} for free space {} and file size {}", numOfFiles, (SystemUtil.getFreeSpace(fileDrive)), fileSizeBytes);
        }

        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
    }

    private void executeStaticInThreadPool(File fileDrive, String driveDumpDir, int threads, long fileSizeBytes, String fileNameSuffix) throws InterruptedException {
        int numOfFiles = (int) (SystemUtil.getFreeSpace(fileDrive) / fileSizeBytes);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        IntStream.rangeClosed(1, numOfFiles).forEach(iFile -> {
            executor.submit(() -> {
                FileUtil.createFileStatic(driveDumpDir, iFile, fileNameSuffix);
                return null;
            });
            Util.sleepMillisSilent(10);
        });

        while (executor.getTaskCount() != executor.getCompletedTaskCount()) {
            log.info("Execution tasks for file size {} completed/total [{}/{}]", fileNameSuffix, executor.getCompletedTaskCount(), executor.getTaskCount());
            Util.sleepSecondsSilent(60);
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
    }

    private void startDumpRandomFiles4(File fileDrive, String driveDumpDir, int threads) throws InterruptedException {
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 100, "400MB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 10, "40MB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 1, "4MB");

        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 100, "400KB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 10, "40KB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 1, "4KB");
    }

    private void startDumpRandomFiles10(File fileDrive, String driveDumpDir, int threads) throws InterruptedException {
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 100, "100MB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 10, "10MB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 1, "1MB");

        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 100, "100KB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 10, "10KB");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 1, "1KB");

        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 100, "100B");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 10, "10B");
        executeRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 1, "1B");
    }

    private void startDumpTunedRandomFiles4(File fileDrive, String driveDumpDir, int threads) throws InterruptedException {
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4GB * 1, "4GB");

        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 100, "400MB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 10, "40MB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 1, "4MB");

        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 100, "400KB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 10, "40KB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 1, "4KB");
    }

    private void startDumpTunedRandomFiles10(File fileDrive, String driveDumpDir, int threads) throws InterruptedException {
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1GB * 1, "1GB");

        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 100, "100MB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 10, "10MB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 1, "1MB");

        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 100, "100KB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 10, "10KB");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 1, "1KB");

        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 100, "100B");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 10, "10B");
        executeTunedRandomInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 1, "1B");
    }

    private void startDumpStaticFiles4(File fileDrive, String driveDumpDir, int threads) throws InterruptedException {
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4GB * 1, "4GB");

        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 100, "400MB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 10, "40MB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4MB * 1, "4MB");

        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 100, "400KB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 10, "40KB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES4KB * 1, "4KB");
    }

    private void startDumpStaticFiles10(File fileDrive, String driveDumpDir, int threads) throws InterruptedException {
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1GB * 1, "1GB");

        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 100, "100MB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 10, "10MB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1MB * 1, "1MB");

        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 100, "100KB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 10, "10KB");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1KB * 1, "1KB");

        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 100, "100B");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 10, "10B");
        executeStaticInThreadPool(fileDrive, driveDumpDir, threads, FileUtil.BYTES1B * 1, "1B");
    }
}
