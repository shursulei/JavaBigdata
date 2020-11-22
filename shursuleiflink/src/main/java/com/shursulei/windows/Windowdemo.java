package com.shursulei.windows;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.runtime.operators.window.assigners.SessionWindowAssigner;

import java.time.Duration;
import java.util.stream.Stream;

public class Windowdemo {
    public static void main(String[] args){
        // set up execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> text =env.socketTextStream("Centos",9999);
        //滚动窗口
        text.keyBy("AAA").timeWindow(Time.minutes(43200));
        //滑动窗口
        text.keyBy("BBB").timeWindow(Time.minutes(1),Time.seconds(30));
        //计数窗口
        text.keyBy("CCC").countWindow(4,2);
        //会话窗口
//        WindowedStream<String, Tuple, W> aaa = text.keyBy("AAA").window(SessionWindowAssigner.withGap(Duration.ZERO));
    }
}
