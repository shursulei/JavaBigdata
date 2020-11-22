import java.util.Properties

import breeze.linalg.min
import org.apache.spark.sql.{DataFrame, SparkSession}
import spire.syntax.numeric
import org.apache.spark.storage.StorageLevel
object SparkMysql{
  private val session: SparkSession = SparkSession.builder().appName("My First Spark Application!").master("local[2]").getOrCreate()
  def main(args: Array[String]): Unit = {
//    var prescriptionsql = "select * from ih.prescription"
//    var base_user_infsql = "select * from user_dataset.base_user_info"
//    val prescriptiondf: DataFrame = SparkMysql.readMysqlTableBysql(prescriptionsql)
//    val base_user_infodf: DataFrame = SparkMysql.readMysqlTableBysql(base_user_infsql)
//    var sql="SELECT DATE_FORMAT(ad.gmt_create, '%Y-%m') AS '月份',u.tag,COUNT(DISTINCT ad.doctor_id) AS '医生数',COUNT(DISTINCT ad.patient_id) AS '咨询人数',COUNT(*) AS '咨询数' FROM medical.patient_advisory_info ad LEFT JOIN user_dataset.base_user_info u ON ad.patient_id = u.id WHERE u.utype = 2 AND u.tag IS NOT NULL GROUP BY DATE_FORMAT(ad.gmt_create, '%Y-%m'),u.tag"
//    var sql="select * from ih.prescription_detail"
    var sql="""
    SELECT
    first_month,
    MONTH,
    count( DISTINCT aa.`doctor_id` ) doctor_num
    FROM
    (
      SELECT
        o.doctor_id,
    min(
      DATE_FORMAT( o.`gmt_create`, '%Y%m' )) first_month
    FROM
    medical.patient_advisory_info o
      --   WHERE o.gmt_create >= '2018-01-01'
    GROUP BY
      o.`doctor_id`
    ) aa
    JOIN (
      SELECT DISTINCT
        oo.doctor_id,
      DATE_FORMAT( oo.`gmt_create`, '%Y%m' ) MONTH
    FROM
    medical.patient_advisory_info oo
      --   WHERE oo.gmt_create >= '2018-01-01'
    ) bb ON aa.doctor_id = bb.doctor_id
    WHERE
    first_month <= MONTH
    GROUP BY
      first_month,MONTH
    """.stripMargin

//      "select * from medical.patient_advisory_info"
    SparkMysql.readMysqlTableBysql(sql).createOrReplaceTempView("test")
    val constantdf:DataFrame=SparkMysql.readMysqlTableBysql(sql)
//    constantdf.persist(new StorageLevel(false, true, false, true))
//    constantdf.write
//    print(constantdf.count())
//    val fcount =constantdf.count()
//    print(fcount)
    constantdf.show(20)
//    prescriptiondf.printSchema()
//    base_user_infodf.printSchema()
//    val base_user_infodf2=base_user_infodf.where("utype=2").filter("referer IS NULL")
//    prescriptiondf.join(base_user_infodf2,prescriptiondf("patient_id") === base_user_infodf("id"),"left").show(10)

//      .groupBy("DATE_FORMAT(gmt_create, '%Y-%m')").count().show()
//    prescriptiondf.join(base_user_infodf,Seq("")).where()

  }
  //    frame.cache()
  //    frame.select("月份").show()
  //    frame.show(200)
  //    frame.join()
  def readMysqlTableBysql(sql:String):DataFrame ={
    var tableName = "("+sql+") as t1"
    val properties = new Properties()
    session.read.format("jdbc")
      .option("url",Constant.MYSQL_URL)
      .option("driver",Constant.MYSQL_DRIVER)
      .option("user",Constant.MYSQL_USERNAME)
      .option("password",Constant.MYSQL_PASSWORD)
      .option("dbtable",tableName)
      .option("numPartitions",20)
      .option("partitionColumn","doctor_num")
      .option("lowerBound", "0")
      .option("upperBound","20000")
      .load()

  }
  def readMysqlTable(table:String,filterCondition:String):DataFrame ={
    val sql="SELECT DATE_FORMAT(pres.gmt_create, '%Y-%m') AS '月份',COUNT(DISTINCT pres.patient_id) AS '非医院处方用户数',COUNT(pres.gmt_create) AS '非医院处方数' FROM ih.prescription pres LEFT JOIN user_dataset.base_user_info bui ON pres.patient_id = bui.id WHERE bui.utype = 2 AND bui.referer IS NULL  GROUP BY DATE_FORMAT(pres.gmt_create, '%Y-%m')"
    val sql2="SELECT * FROM ih.prescription"
//    var tableName = "(select * from "+table+" where "+ filterCondition+" ) as t1"
    var tableName = "("+sql2+") as t1"
    val properties = new Properties()
    session.read.format("jdbc")
      .option("url",Constant.MYSQL_URL)
      .option("driver",Constant.MYSQL_DRIVER)
      .option("user",Constant.MYSQL_USERNAME)
      .option("password",Constant.MYSQL_PASSWORD)
      .option("dbtable",tableName)
      .option("fetchsize",100000)
      .load()

  }
}