package com.jibi.filldisk;

import com.jibi.filldisk.service.FillDiskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@Slf4j
public class FillDiskApplication implements CommandLineRunner {

    @Autowired
    private FillDiskService fillDiskService;

    private Options options;
    private CommandLineParser parser;
    private CommandLine cmd;
    private HelpFormatter formatter;

    @PostConstruct
    public void init() {
        options = new Options();
        parser = new DefaultParser();
        formatter = new HelpFormatter();

        final Option fileOption = Option.builder("d")
                .argName("drive").optionalArg(false)
                .hasArg()
                .desc("Drive to be be filled")
                .build();

        options.addOption(fileOption);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application Started !!");

        CommandLine cmd = parser.parse(options, args);

        String driveLetter = cmd.getOptionValue("d");

        fillDiskService.fillDrive(driveLetter);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(FillDiskApplication.class, args);

        /*
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
*/
    }



}
