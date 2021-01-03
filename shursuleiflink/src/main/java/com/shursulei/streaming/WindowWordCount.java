package com.shursulei.streaming;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class WindowWordCount {
    public static void main(String args) {
        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Tuple2<String,Integer>> dataStream = env.socketTextStream("localhost",9999).flatMap(new Splitter()).keyBy(0).timeWindow(Time.seconds(5))
                .sum(1);
    }

    private static class Splitter implements FlatMapFunction<String,Tuple2<String,Integer>>{
        @Override
        public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) throws Exception {
            for (String word:sentence.split("  ")) {
                out.collect(new Tuple2<String, Integer>(word,1));
            }

        }
    }
}
