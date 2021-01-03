package com.shursulei.table;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class demo1 {
    public static void main(String[] args){
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.createLocalEnvironment();
        env.setParallelism(1);
        DataStream<String> text=env.readTextFile("C:\\sasworkspace\\software\\ideawork\\flinkdemo\\src\\main\\java\\com\\shursulei\\finkdemo1\\demo1");
        DataStream<Integer> parsed = text.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String value) {
                return Integer.parseInt(value);
            }
        });
        parsed.print();
        try {
            env.execute("demo1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
