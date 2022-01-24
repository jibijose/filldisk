package com.jibi.filldisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class FillDiskApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FillDiskApplication.class, args);

        FileSystemView fsv = FileSystemView.getFileSystemView();

        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                System.out.println("Drive Letter: " + aDrive);
                System.out.println("\tType: " + fsv.getSystemTypeDescription(aDrive));
                System.out.println("\tTotal space: " + aDrive.getTotalSpace());
                System.out.println("\tFree space: " + aDrive.getFreeSpace());
                System.out.println();
            }
        }

        String drive = "C:\\";
        File fileDrive = new File(drive);

        String driveDumpDir = drive + "\\DUMPDIR";
        File theDir = new File(driveDumpDir);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        while (getFreeSpace(fileDrive) / 1024 / 1024 / 1024 > 0) {
            createFile("1GB.txt", getDateTimeFormatted() + "-1GB.txt");
        }

        while (getFreeSpace(fileDrive) / 1024 / 1024 / 100 > 0) {
            createFile("100MB.txt", getDateTimeFormatted() + "-100MB.txt");
        }
        while (getFreeSpace(fileDrive) / 1024 / 1024 / 10 > 0) {
            createFile("10MB.txt", getDateTimeFormatted() + "-10MB.txt");
        }
        while (getFreeSpace(fileDrive) / 1024 / 1024 > 0) {
            createFile("1MB.txt", getDateTimeFormatted() + "-1MB.txt");
        }

        while (getFreeSpace(fileDrive) / 1024 / 100 > 0) {
            createFile("100KB.txt", getDateTimeFormatted() + "-100KB.txt");
        }
        while (getFreeSpace(fileDrive) / 1024 / 10 > 0) {
            createFile("10KB.txt", getDateTimeFormatted() + "-10KB.txt");
        }
        while (getFreeSpace(fileDrive) / 1024 > 0) {
            createFile("1KB.txt", getDateTimeFormatted() + "-1KB.txt");
        }

        while (getFreeSpace(fileDrive) / 100 > 0) {
            createFile("100B.txt", getDateTimeFormatted() + "-100B.txt");
        }
        while (getFreeSpace(fileDrive) / 10 > 0) {
            createFile("10B.txt", getDateTimeFormatted() + "-10B.txt");
        }
        while (getFreeSpace(fileDrive) > 0) {
            createFile("1B.txt", getDateTimeFormatted() + "-1B.txt");
        }

    }

    private static long getFreeSpace(File file) {
        return file.getFreeSpace();
    }

    private static String getDateTimeFormatted() {
        String pattern = "yyyyMMdd-HHmmssSSS";
        return new SimpleDateFormat(pattern).format(new Date());
    }


    private static void createFile(final String fromFilename, final String toFilename) throws Exception {
        Path copied = Paths.get("C:\\DUMPDIR\\" + toFilename);
        Path originalPath = Paths.get("src/main/resources/" + fromFilename);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        Thread.sleep(1);
    }

}
