package com.shursulei.biplatform.services.designer

import java.util.UUID

import akka.NotUsed
import akka.actor.ActorRef
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.marshalling.{Marshaller, Marshalling}
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.scaladsl.Source
import akka.stream.{Graph, SourceShape}
import akka.util.ByteString
import com.shursulei.biplatform.common.api.{CirceSupport, Error, SessionSupport}

import com.shursulei.biplatform.queryrouter.{DataCell, DataRow}
import com.shursulei.biplatform.queryrouter.DataCellType._
import com.shursulei.biplatform.system.ReportNode
import io.circe.generic.auto._

import scala.concurrent.duration._

/**
 * @author shursulei
 */
trait DownLoadRoutes extends CirceSupport with SessionSupport {

  val users: ActorRef

  implicit val dataRowAsCsv = Marshaller.strict[DataRow, ByteString] { r ⇒
    Marshalling.WithFixedContentType(ContentTypes.`text/csv(UTF-8)`, () ⇒ {
      val values = r.cells.map {
        case DataCell(value, ROW_HEADER_HEADER, props) ⇒
          Option(props).flatMap(_.get("caption")).filter(_.nonEmpty).getOrElse(value)
        case DataCell(value, COLUMN_HEADER, props) ⇒
          Option(props).flatMap(_.get("caption")).filter(_.nonEmpty).getOrElse(value)
        case c ⇒
          c.value
      }
      ByteString(values.mkString(","))
    })
  }

  implicit val csvStreaming = EntityStreamingSupport.csv()

  val downLoadRoutes = (get & path("designer" / "reports" / "download" / Segment)) { id ⇒
    userFromSession { user ⇒
      val reportId = UUID.fromString(id)
      val cmd = ReportNode.Execute(user.login, reportId)
      onSuccess((users ? cmd)(60 seconds)) {
        case graph: Graph[SourceShape[DataRow], NotUsed] ⇒
          val source: Source[DataRow, NotUsed] = Source.fromGraph[DataRow, NotUsed](graph)
          complete(source)
        case error: Error ⇒
          complete(error)
      }
    }
  }
}
