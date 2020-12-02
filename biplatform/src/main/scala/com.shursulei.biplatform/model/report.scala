package com.shursulei.biplatform.model

import java.util.UUID

import cats.data._
import cats.implicits._
import org.joda.time.DateTime

/**
 * @author souo
 */

case class ReportMeta(
  id:            UUID,
  name:          String,
  createBy:      String,
  createTime:    DateTime,
  modifyBy:      Option[String],
  latModifyTime: Option[DateTime],
  saved:         Boolean,
  published:     Boolean
) extends Serializable

case class QueryModel(
    tableName:  String,
    dimensions: List[Dimension],
    measures:   List[Measure],
    filters:    Option[List[Filter]] = None
) {

  def checkTableName: ValidatedNel[String, Boolean] = {
    if (tableName.isEmpty) {
      Validated.invalid("表名不能为空")
    }
    else {
      Validated.valid(true)
    }
  }.toValidatedNel

  def checkDimensionsAndMeasures: ValidatedNel[String, Boolean] = {
    if (dimensions.isEmpty || measures.isEmpty) {
      Validated.invalid("至少指定一个维度和一个指标")
    }
    else {
      Validated.valid(true)
    }
  }.toValidatedNel

  def check(): ValidatedNel[String, Boolean] = {
    checkTableName |+| checkDimensionsAndMeasures
  }
}

case class ReportQuery(
  queryModel: QueryModel,
  cubeId:     UUID,
  properties: Map[String, String]
)

