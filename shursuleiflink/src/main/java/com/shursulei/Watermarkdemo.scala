package com.shursulei

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.watermark.Watermark

class Watermarkdemo {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val input=List(("a",1L,1),("b",1L,1),("b",3L,1))
    val source:DataStream[(String,Long,Int)]=env.addSource(
      new SourceFunction[(String,Long,Int)] () {
        override def run(ctx: SourceFunction.SourceContext[(String, Long, Int)]): Unit = {
          input.foreach(value=>{
            ctx.collectWithTimestamp(value,value._2)
            ctx.emitWatermark(new Watermark(value._2-1))
          })
          //
          ctx.emitWatermark(new Watermark(Long.MaxValue))
        }

        override def cancel(): Unit = {}
        })
  }

}
