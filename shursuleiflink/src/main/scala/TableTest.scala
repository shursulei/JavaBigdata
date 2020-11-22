//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
//import org.apache.flink.table.api.{EnvironmentSettings, Table}
//import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
//
//
//object TableTest{
//  def main(args: Array[String]): Unit = {
//    val fsSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
//    val fsEnv = StreamExecutionEnvironment.getExecutionEnvironment
//    val fsTableEnv = StreamTableEnvironment.create(fsEnv, fsSettings)
//    fsTableEnv.useCatalog("shursulei_catalog")
//    fsTableEnv.useDatabase("shursulei_database")
////    val projTable:Table = fsTableEnv.from("X").select($("key"), $("value").avg().plus(" The average").as("average"))
////    fsTableEnv.createTemporaryView("projectedTable",projTable)
//  }
//
//}