package com.shursulei.springbase.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduledTaskService {
    private  final static SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
    @Scheduled(fixedRate=5000)
    public  void reportCurrentTime(){
        System.out.println("每五秒钟执行一次"+dateFormat.format(new Date()));
    }
    @Scheduled(cron="0 5 11 ? * *")
    public  void fixTimeExecution(){
        System.out.println("在指定时间内"+dateFormat.format(new Date())+"执行");
    }
}
