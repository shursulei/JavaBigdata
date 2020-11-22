
import org.apache.spark.sql.{DataFrame, SparkSession}
object SparkMysqlTest{
  private val session: SparkSession = SparkSession.builder().appName("mysqlread").master("local[2]").getOrCreate()
  def main(args: Array[String]): Unit = {
//    val tablename="(select id,gmt_create from medical.patient_advisory_info) as patient_advisory_info"
    val tablename="medical.patient_advisory_info"
//    SparkMysqlTest.readMysqlTableBysql(tablename).createOrReplaceTempView("patient_advisory_info")
    val constantdf:DataFrame=SparkMysqlTest.readMysqlTableBysql(tablename)
    constantdf.show(20)
  }
  def readMysqlTableBysql(tableName:String):DataFrame ={
    session.read.format("jdbc")
      .option("url",Constant.MYSQL_URL)
      .option("driver",Constant.MYSQL_DRIVER)
      .option("user",Constant.MYSQL_USERNAME)
      .option("password",Constant.MYSQL_PASSWORD)
      .option("dbtable","(select * from ih.prescription where gmt_create>='2019-01-01' and gmt_create<'2020-01-01') AS T")
      .option("fetchSize",100000)
      .option("numPartitions",200)
      .option("partitionColumn","id")
      .option("lowerBound","10000")
      .option("upperBound","40000")
      .load()
  }
}