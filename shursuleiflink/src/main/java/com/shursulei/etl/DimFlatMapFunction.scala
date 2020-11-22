//package com.shursulei.etl
//
//import java.sql
//import java.sql.DriverManager
//
//import org.apache.flink.api.common.functions.RichFlatMapFunction
//import org.apache.flink.configuration.Configuration
//import org.apache.flink.runtime.taskexecutor.JobTable.Connection
//import org.apache.flink.util.Collector
//
//class DimFlatMapFunction extends RichFlatMapFunction[(Int, String), (Int, String, String)] {
//  var dim:Map[Int,String]=Map()
//  var connection:sql.Connection=_
//  override def open(conf:Configuration):Unit={
//    super.open(conf)
//    Class.forName("com.mysql.cj.jdbc.Driver")
//    val url="jdbc:mysql:"
//    val username=""
//    val password=""
//    connection=DriverManager.getConnection(url,username,password)
//    val sql="select ";
//    val statement=connection.prepareStatement(sql)
//    try{
//      var resultSet=statement.executeQuery()
//      while (resultSet.next()){
//        var pid=resultSet.getInt("")
//        var pname=resultSet.getInt("")
//        dim += (pid->pname)
//      }
//    }catch {
//      case e:Exception=>println(e.getMessage)
//    }
//    connection.close()
//  }
//  override def flatMap(in: (Int, String), out: Collector[(Int, String, String)]): Unit = {
//    val probeID=in._1
//    if(dim.contains(probeID)){
//      out.collect((in._1,in._2,dim.get(probeID).toString))
//    }
//  }
//}
