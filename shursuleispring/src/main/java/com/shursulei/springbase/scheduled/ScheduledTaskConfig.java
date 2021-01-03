package com.shursulei.springbase.scheduled;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("com.shursulei.springbase.scheduled")
@EnableScheduling
public class ScheduledTaskConfig {
}
