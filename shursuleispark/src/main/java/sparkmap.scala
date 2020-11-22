import org.apache.spark.sql
import org.apache.spark.sql.{DataFrame, SparkSession}

object sparkmap {
  def main(args: Array[String]): Unit = {
    val spark=new sql.SparkSession
    .Builder()
      .appName("source_data_mysql001")
      .master("local")
      .getOrCreate()
    val jdbc_conf: Map[String, String] = Map(
      "url" -> "jdbc:mysql://localhost:3306/table147",   //设置mysql的链接地址和指定数据库
      "driver" -> "com.mysql.jdbc.Driver",    //设置MySQL的链接驱动
      "dbtable" -> "people01",      //获取数据所在表的名成
      "user" -> "root",        //连接mysql的用户
      "password" -> "111111"   //连接用户的密码
    )
    val data_mysql: DataFrame = spark.read.format("jdbc")   //设置读取方式
      .options(jdbc_conf)    //放入jdbc的配置信息
      .load()

    data_mysql.show()   //使用一个action算子来检查是否能读取数据
  }
}