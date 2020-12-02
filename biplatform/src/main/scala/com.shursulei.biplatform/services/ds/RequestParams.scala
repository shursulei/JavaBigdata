package com.shursulei.biplatform.services.ds

/**
 * @author souo
 */
object RequestParams {

  case class Csv(
    name: String,
    path: String,
    sep:  String = ","
  )

  case class Mysql(
    name: String,
    host: String,
    port: Int,
    db:   String,
    user: String,
    pwd:  String
  )

  case class H2(
    name: String,
    url:  String,
    user: Option[String],
    pwd:  Option[String]
  )

}
