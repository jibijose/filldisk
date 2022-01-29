package com.jibi.filldisk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

@Slf4j
public class SystemUtil {

    public static void getDrivesInfo() {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                log.debug("Drive Letter: {}", aDrive);
                log.debug("\tType: {}", fsv.getSystemTypeDescription(aDrive));
                log.debug("\tTotal space: {}", aDrive.getTotalSpace());
                log.debug("\tFree space: {}", aDrive.getFreeSpace());
                log.debug("*******************************************************");
            }
        }
    }

    public static boolean isWindowsOS() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return true;
        } else {
            return false;
        }
    }

    public static long getFreeSpace(File file) {
        return file.getFreeSpace();
    }


    public static String getFormattedFreeSpace(File file) {
        long bytesLeft = file.getFreeSpace();
        long counterBytesLeft = bytesLeft;
        long GB_COUNT = 0;
        long MB_COUNT = 0;
        long KB_COUNT = 0;
        long B_COUNT = 0;

        if (counterBytesLeft >= FileUtil.BYTES1GB) {
            GB_COUNT = counterBytesLeft / FileUtil.BYTES1GB;
            counterBytesLeft = counterBytesLeft % FileUtil.BYTES1GB;
        }
        if (counterBytesLeft >= FileUtil.BYTES1MB) {
            MB_COUNT = counterBytesLeft / FileUtil.BYTES1MB;
            counterBytesLeft = counterBytesLeft % FileUtil.BYTES1MB;
        }
        if (counterBytesLeft >= FileUtil.BYTES1KB) {
            KB_COUNT = counterBytesLeft / FileUtil.BYTES1KB;
            counterBytesLeft = counterBytesLeft % FileUtil.BYTES1KB;
        }
        if (counterBytesLeft >= FileUtil.BYTES1B) {
            B_COUNT = counterBytesLeft / FileUtil.BYTES1B;
            counterBytesLeft = counterBytesLeft % FileUtil.BYTES1B;
        }
        String formattedBytes = "";
        if (GB_COUNT > 0) {
            formattedBytes = formattedBytes + String.format("%s GB ", GB_COUNT);
        }
        if (MB_COUNT > 0) {
            formattedBytes = formattedBytes + String.format("%s MB ", MB_COUNT);
        }
        if (KB_COUNT > 0) {
            formattedBytes = formattedBytes + String.format("%s KB ", KB_COUNT);
        }
        if (B_COUNT > 0) {
            formattedBytes = formattedBytes + String.format("%s B ", B_COUNT);
        }

        log.debug("Starting bytes {}, left out bytes {}, formatted bytes [{}]", bytesLeft, counterBytesLeft, formattedBytes);
        return formattedBytes;
    }

}
