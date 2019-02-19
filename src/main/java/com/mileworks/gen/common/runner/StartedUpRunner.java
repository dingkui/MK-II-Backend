package com.mileworks.gen.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Order
@Slf4j
@Component
public class StartedUpRunner implements CommandLineRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void run(String... args) {
        if (context.isActive()) {
            log.info(" _____ _____ _____ _____ _____ _____ ");
            log.info("|   __|     |   | |     |   __|  |  |");
            log.info("|   __|-   -| | | |-   -|__   |     |");
            log.info("|__|  |_____|_|___|_____|_____|__|__|");
            log.info("                                                      ");
            log.info("MK-II 启动完毕，时间：" + LocalDateTime.now());
        }
    }
}
