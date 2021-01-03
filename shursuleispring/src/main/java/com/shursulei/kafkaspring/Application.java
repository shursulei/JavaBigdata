package com.shursulei.kafkaspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class Application implements CommandLineRunner {
    public static Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }
    @Autowired
//    private KafkaTemplate<String, String> template;

    private final CountDownLatch latch = new CountDownLatch(3);
    @Override
    public void run(String... args) throws Exception {

    }
}
