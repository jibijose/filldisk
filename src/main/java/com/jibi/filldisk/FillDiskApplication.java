package com.jibi.filldisk;

import com.jibi.filldisk.service.FillDiskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

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

        final Option driveOption = Option.builder("d")
                .argName("drive").optionalArg(false)
                .hasArg()
                .desc("Drive to be be filled")
                .build();
        final Option fillSizeOption = Option.builder("f")
                .argName("fillesize").optionalArg(false)
                .hasArg()
                .desc("Fill size")
                .build();
        final Option randomDataOption = Option.builder("r")
                .argName("randomdata").optionalArg(true)
                .hasArg()
                .desc("Random Data")
                .build();
        final Option threadsOption = Option.builder("t")
                .argName("threads").optionalArg(true)
                .hasArg()
                .desc("Number of threads")
                .build();

        options.addOption(driveOption);
        options.addOption(fillSizeOption);
        options.addOption(randomDataOption);
        options.addOption(threadsOption);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application Started !!");

        CommandLine cmd = parser.parse(options, args);

        String driveLetter;
        if (cmd.getOptionValue("d") != null) {
            driveLetter = cmd.getOptionValue("d");
        } else {
            throw new RuntimeException("Drive is needed");
        }

        int fillSize = -1;
        if (cmd.getOptionValue("f") != null) {
            fillSize = Integer.parseInt(cmd.getOptionValue("f"));
        } else {
            throw new RuntimeException("Fill size is needed");
        }

        boolean randomData = false;
        if (cmd.getOptionValue("r") != null) {
            randomData = Boolean.parseBoolean(cmd.getOptionValue("r"));
        }

        int threads = 1;
        if (cmd.getOptionValue("t") != null) {
            threads = Integer.parseInt(cmd.getOptionValue("t"));
        } else {
            int processors = Runtime.getRuntime().availableProcessors();
            if (processors >= 2) {
                threads = processors / 2;
            }
        }
        log.info("Application started with drive={} fillSize={} randomData={} threads={}", driveLetter, fillSize, randomData, threads);

        if (randomData) {
            fillDiskService.fillDriveRandom(driveLetter, fillSize, threads);
        } else {
            fillDiskService.fillDriveStatic(driveLetter, fillSize, threads);
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(FillDiskApplication.class, args);
    }


}
