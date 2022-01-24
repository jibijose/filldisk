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
    }



}
