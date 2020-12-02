package com.shursulei.biplatform.model

import java.sql.{Connection, DriverManager}

import cats.syntax.either._
import com.mysql.cj.jdbc.{Driver ⇒ MysqlDriver}
import com.typesafe.scalalogging.StrictLogging
import org.apache.kylin.jdbc.{Driver ⇒ KylinDriver}
import org.h2.{Driver ⇒ H2Driver}

/**
 * @author shursulei
 */
trait DataSource

trait DataSourceFunction {
  def listAllTables(): Result[List[TableInfo]]
  def listAllColumns(tableId: String): Result[List[ColumnInfo]]
}

trait JdbcSource extends DataSource {
  val url: String
  val user: String
  val pwd: String
  val driver: String
}

trait JdbcSourceFunctionImpl extends DataSourceFunction with StrictLogging {
  this: JdbcSource =>

  def getConnection: Result[Connection] = {
    Either.catchNonFatal[Connection] {
      logger.info(s"load driver:$driver")
      Class.forName(driver)
      DriverManager.getConnection(url, user, pwd)
    }
  }

  def withDb[T](f: (Connection) => Result[T]): Result[T] = {
    logger.info("open jdbc connection")
    val connection = getConnection
    try {
      connection.flatMap(f)
    }
    finally {
      connection.foreach { c ⇒
        logger.info("closing jdbc connection...")
        c.close()
      }
    }
  }

  override def listAllTables(): Result[List[TableInfo]] = {
    withDb { connection ⇒
      Either.catchNonFatal {
        val metaData = connection.getMetaData
        val rs = metaData.getTables(null, null, "%", Array("TABLE"))
        var result = List.empty[TableInfo]
        while (rs != null && rs.next) {
          val tableName = rs.getString("TABLE_NAME")
          val comment = rs.getString("REMARKS")
          result ::= TableInfo(tableName, tableName, comment)
        }
        result
      }
    }
  }

  override def listAllColumns(tableId: String): Result[List[ColumnInfo]] = {
    withDb { connection ⇒
      Either.catchNonFatal {
        val metaData = connection.getMetaData
        val rs = metaData.getColumns(null, null, tableId, "%")
        var result = List.empty[ColumnInfo]
        while (rs != null && rs.next) {
          val tableName = rs.getString("COLUMN_NAME")
          val comment = rs.getString("REMARKS")
          val dt = rs.getString("TYPE_NAME")
          result ::= ColumnInfo(tableName, tableName, comment, dt)
        }
        result
      }
    }
  }
}

case class MySqlSource(
    host: String,
    port: Int,
    db:   String,
    user: String,
    pwd:  String
) extends JdbcSource {

  override val url: String = {
    s"jdbc:mysql://$host:$port/$db"
  }
  override val driver: String = classOf[MysqlDriver].getCanonicalName
}

case class KylinSource(
    host: String,
    port: Int,
    db:   String,
    user: String,
    pwd:  String
) extends JdbcSource {

  override val url: String = {
    s"jdbc:kylin://$host:$port/$db"
  }

  override val driver: String = classOf[KylinDriver].getCanonicalName
}

case class H2Source(
    url:  String,
    user: String = "",
    pwd:  String = ""
) extends JdbcSource {
  override val driver: String = classOf[H2Driver].getCanonicalName

}

case class TableInfo(id: String, name: String, comment: String)

case class ColumnInfo(id: String, name: String, comment: String, dataType: String)

object DataSources {
  def mysql(
    host: String,
    port: Int,
    db:   String,
    user: String,
    pwd:  String
  ): MySqlSource = {
    MySqlSource(host, port, db, user, pwd)
  }

  def h2(
    url:  String,
    user: String = "",
    pwd:  String = ""
  ): H2Source = {
    H2Source(url, user, pwd)
  }
}

case class DsServer(dataSource: DataSource) extends DataSourceFunction {

  val dsInstance = dataSource match {
    case jdbc: JdbcSource ⇒
      new JdbcSource with JdbcSourceFunctionImpl {
        override val url: String = jdbc.url
        override val driver: String = jdbc.driver
        override val pwd: String = jdbc.pwd
        override val user: String = jdbc.user
      }
  }

  override def listAllTables(): Either[Throwable, List[TableInfo]] = {
    dsInstance.listAllTables()
  }

  override def listAllColumns(tableId: String): Either[Throwable, List[ColumnInfo]] = {
    dsInstance.listAllColumns(tableId)
  }
}

