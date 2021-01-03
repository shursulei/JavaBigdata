package com.shursulei.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class flinkdemo {

    public void addSource(){
        final LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
//        env.addSource("");
    }

    public void collectionE() {
         final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
         // Create a DataStream from a list of elements
        DataStream<Integer> myInts = env.fromElements(1, 2, 3, 4, 5);
         // Create a DataStream from any Java collection
//        DataStream<Tuple2<String, Integer>> myTuples = (DataStream<Tuple2<String, Integer>>) env.fromCollection(myInts);
         // Create a DataStream from an Iterator
//        Iterator<Long> longIt = ...
//        DataStream<Long> myLongs = env.fromCollection(longIt, Long.class);


    }

}
