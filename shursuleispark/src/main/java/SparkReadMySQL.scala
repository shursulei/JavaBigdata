import java.util.{Properties, UUID}
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.SparkConf

object SparkReadMySQL {

  def main(args:Array[String]):Unit = {
    val appNameSuffix = UUID.randomUUID()

    val config = new SparkConf().setMaster("local[*]").setAppName("SparkReadMySQL_"+appNameSuffix)
    val spark = SparkSession.builder.config(config).getOrCreate()

    import spark.implicits._

    val jdbcUrl = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false"
    def dbConnProperties(user:String, pass:String):Properties = {
      val ConnProperties = new Properties();
      ConnProperties.put("driver", "com.mysql.jdbc.Driver");
      ConnProperties.put("user", user);
      ConnProperties.put("password", pass);
      ConnProperties.put("fetchsize", "1000");  //读取条数限制
      ConnProperties.put("batchsize", "10000"); //写入条数限制
      return ConnProperties;
    }
    val dbUser = "root"
    val dbPass = "123456"

    val readConnProperties = dbConnProperties(dbUser,dbPass);

    val sql = "select a.* ,b.week from dm_raw_data a left join dm_week_alignment b on a.day = b.day where b.week between '20180901' and '20180902'"

    val df = spark.read.jdbc(jdbcUrl, s"(${sql}) t",readConnProperties)

    // 显示前100行
    df.show(100)

    val toPath = "/data1/tmp"

    //合并为一个文件输出
    df.coalesce(1).write.format("csv").option("charset","utf-8").option("delimiter", ',')
      .mode(SaveMode.Append).save(toPath)

    //直接输出
    df.write.format("csv").option("charset","utf-8").option("delimiter", ',')
      .mode(SaveMode.Append).save(toPath)

    spark.stop()
  }
}