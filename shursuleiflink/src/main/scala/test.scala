//import com.shursulei.test.{JdbcReader, JdbcWriter}
//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
//import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
//
//object test{
//  def main(args: Array[String]): Unit = {
//    //scala代码
//    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    env.enableCheckpointing(5000)
//    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
//    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
//    val dataStream =  env.addSource(new JdbcReader())//，读取mysql数据，获取dataStream后可以做逻辑处理，这里没有做
//    dataStream.addSink(new JdbcWriter)//写入mysql
//    env.execute("flink mysql demo")
//  }
//
//}