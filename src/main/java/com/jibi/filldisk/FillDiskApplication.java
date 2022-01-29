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
                .argName("drive").optionalArg(false)
                .hasArg()
                .desc("Fill size")
                .build();

        options.addOption(driveOption);
        options.addOption(fillSizeOption);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application Started !!");

        CommandLine cmd = parser.parse(options, args);

        String driveLetter = cmd.getOptionValue("d");
        int fillSize = -1;
        if ( cmd.getOptionValue("f") != null ) {
            fillSize = Integer.parseInt(cmd.getOptionValue("f"));
        }

        fillDiskService.fillDrive(driveLetter, fillSize);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(FillDiskApplication.class, args);
    }


}
