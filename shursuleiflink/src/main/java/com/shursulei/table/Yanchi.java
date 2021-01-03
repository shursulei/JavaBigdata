package com.shursulei.table;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.sql.Time;

public class Yanchi {
    public static  void main(String[] args) throws Exception {
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        Time.valueOf("10");
//        env.setBufferTimeout(timeoutMillis);
//        env.generateSequence(1,10).map(new MyMapper()).setBufferTimeout(timeoutMillis);
    }

}
