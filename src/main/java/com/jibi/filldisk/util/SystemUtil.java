package com.jibi.filldisk.util;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class SystemUtil {

    public static void getDrivesInfo() {
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
    }

    public static long getFreeSpace(File file) {
        return file.getFreeSpace();
    }

}
