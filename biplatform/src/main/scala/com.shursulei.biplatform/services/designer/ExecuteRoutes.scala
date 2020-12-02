package com.shursulei.biplatform.services.designer

import java.util.UUID

import akka.NotUsed
import akka.actor.ActorRef
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.scaladsl.Source
import akka.stream.{Graph, SourceShape}
import com.shursulei.biplatform.common.api.{CirceSupport, Error, Response, SessionSupport}
import com.shursulei.biplatform.queryrouter.DataRow
import com.shursulei.biplatform.system.ReportNode
import io.circe.generic.auto._
import com.shursulei.biplatform.common.api.Response._

import scala.concurrent.duration._

/**
 * @author shursulei
 */
trait ExecuteRoutes extends CirceSupport with SessionSupport {

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = {
    EntityStreamingSupport.json()
  }
  val users: ActorRef

  val executeRoutes = (post & path("reports" / Segment / "execute")) { id ⇒
    userFromSession { user ⇒
      val reportId = UUID.fromString(id)
      val cmd = ReportNode.Execute(user.login, reportId)
      onSuccess((users ? cmd)(60 seconds)) {
        case graph: Graph[SourceShape[DataRow], NotUsed] ⇒
          val source: Source[DataRow, NotUsed] = Source.fromGraph(graph)
          complete(source)
        case error: Error ⇒
          complete(error)
      }
    }
  }
}
