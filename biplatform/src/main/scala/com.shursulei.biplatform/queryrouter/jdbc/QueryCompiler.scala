
package com.shursulei.biplatform.queryrouter.jdbc

import cats.syntax.either._
import cats.{Id, ~>}
import com.shursulei.biplatform.queryrouter._
import com.shursulei.biplatform.queryrouter.jdbc.QueryBuilder._
import com.shursulei.biplatform.model._

/**
 * @author souo
 */
class QueryCompiler extends (PartBuilder ~> Id) {

  val nameMaps = Map("SUM" → "求和", "COUNT" → "计数", "AVG" → "平均值")

  def makeValue(tpe: String, value: String) = {
    tpe match {
      case "CHAR" | "VARCHAR" | "LONGNVARCHAR" | "TIMESTAMP" | "DATE" | "TIME" ⇒
        s"'$value'"
      case _ ⇒ value
    }
  }

  override def apply[A](fa: PartBuilder[A]): Id[A] = fa match {
    case MeasuresBuilder(measures) ⇒
      Either.catchNonFatal {
        measures.map {
          case Measure(dim, aggregator) ⇒
            aggregator match {
              case "DISTINCT_COUNT" ⇒
                val sqlName = s"COUNT(DISTINCT(${dim.name}))"
                val caption = {
                  if (dim.caption.isEmpty) {
                    dim.name
                  }
                  else {
                    dim.caption
                  }
                } + "去重计数"
                SqlField(dim.name, sqlName, caption)
              case "SUM" | "COUNT" | "AVG" ⇒
                val sqlName = s"$aggregator(${dim.name})"
                val caption = {
                  if (dim.caption.isEmpty) {
                    dim.name
                  }
                  else {
                    dim.caption
                  }
                } + nameMaps(aggregator)
                SqlField(dim.name, sqlName, caption)
              case _ ⇒
                sys.error(s"UnSupport measure aggregator $aggregator")
            }
        }
      }.asInstanceOf[A]

    case DimensionsBuilder(dimensions) ⇒
      Either.right[Throwable, Seq[SqlField]] {
        dimensions.map(d ⇒ SqlField(d.name, d.name, d.caption))
      }.asInstanceOf[A]

    case FilterBuilder(filters) ⇒
      Either.catchNonFatal {
        filters.filter(_.nonEmpty).map {
          _.map {
            case gt(dim, value) ⇒
              SqlFilter(dim.name, ">", makeValue(dim.dataType, value))
            case gte(dim, value) ⇒
              SqlFilter(dim.name, ">=", makeValue(dim.dataType, value))
            case lt(dim, value) ⇒
              SqlFilter(dim.name, "<", makeValue(dim.dataType, value))
            case lte(dim, value) ⇒
              SqlFilter(dim.name, "<=", makeValue(dim.dataType, value))
            case eql(dim, value, include) ⇒
              val values = value.split(",")
              val hasMultiValue = values.length > 1
              if (hasMultiValue) {
                val oper = if (include) "IN" else "NOT IN"
                SqlFilter(dim.name, oper, s"(${values.map(makeValue(dim.dataType, _)).mkString(", ")})")
              }
              else {
                val oper = if (include) "=" else "!="
                SqlFilter(dim.name, oper, makeValue(dim.dataType, value))
              }
            case in(dim, value, include) ⇒
              val oper = if (include) "IN" else "NOT IN"
              SqlFilter(dim.name, oper, s"(${value.map(makeValue(dim.dataType, _)).mkString(", ")})")
            case range(dim, value) if value.length == 2 ⇒
              SqlFilter(dim.name, "",
                s"BETWEEN ${makeValue(dim.dataType, value.head)} AND ${makeValue(dim.dataType, value(1))}")
            case other ⇒
              sys.error(s"wrong filter $other")
          }
        }
      }.asInstanceOf[A]
  }
}
