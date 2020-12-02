package com.shursulei.biplatform.services.designer

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import com.typesafe.scalalogging.StrictLogging
import com.shursulei.biplatform.common.api.{DefaultTimeOut, RoutesSupport, SessionSupport}
import com.shursulei.biplatform.common.api.Response._
import com.shursulei.biplatform.model.{ReportMeta, ReportQuery}
import com.shursulei.biplatform.system.{ReportManager, ReportNode}
import io.circe.generic.auto._

/**
 * @author shursulei
 */
trait ReportsRoutes extends RoutesSupport with StrictLogging with SessionSupport with DefaultTimeOut {

  val users: ActorRef

  val reportsRoutes = pathPrefix("reports") {
    (get & pathEnd) {
      userFromSession { user ⇒
        parameter("published" ? false){ published ⇒
          val cmd = ReportManager.ListAllReport(user.login, published)
          onSuccess(users ? cmd) {
            case reports: Array[ReportMeta] ⇒
              complete(ok(reports))
          }
        }
      }
    } ~ (post & pathEnd) {
      userFromSession { user ⇒
        entity(as[RequestParams.CreateReport]) { create ⇒
          val cmd = ReportManager.CreateReport(user.login, create.name)
          onSuccess(users ? cmd) {
            case Right(metas: Array[ReportMeta]) ⇒
              complete(ok(metas))
            case Left(t: Throwable) ⇒
              complete(error(t))
          }
        }
      }
    } ~ (put & pathEnd) {
      userFromSession { user ⇒
        entity(as[RequestParams.UpdateReport]) { update ⇒
          val cmd = ReportManager.UpdateReport(user.login, update.id, update.name)
          onSuccess((users ? cmd).mapTo[Either[Throwable, Array[ReportMeta]]]) {
            case Right(metaList) ⇒
              complete(ok(metaList))
            case Left(t) ⇒
              complete(error(t))
          }
        }
      }
    } ~ (delete & pathEnd) {
      userFromSession { user ⇒
        parameters('reportId) { id ⇒
          val cmd = ReportManager.DropReport(user.login, UUID.fromString(id))
          onSuccess((users ? cmd).mapTo[Either[Throwable, Array[ReportMeta]]]) {
            case Right(metaList) ⇒
              complete(ok(metaList))
            case Left(t) ⇒
              complete(error(t))
          }
        }
      }
    } ~ (get & path(Segment)) { id ⇒
      userFromSession { user ⇒
        val reportId = UUID.fromString(id)
        val cmd = ReportNode.Show(user.login, reportId)
        onSuccess((users ? cmd).mapTo[Option[ReportQuery]]) {
          case Some(r) ⇒
            complete(ok[ReportQuery](r))
          case None ⇒ complete(ok())
        }
      }
    } ~ (post & path(Segment)) { id ⇒
      userFromSession { user ⇒
        val reportId = UUID.fromString(id)
        parameter("publish" ? false) { publish ⇒
          entity(as[ReportQuery]) { query ⇒
            val cmd = if (publish) {
              ReportNode.Publish(user.login, reportId, query)
            }
            else {
              ReportNode.Save(user.login, reportId, query)
            }
            onSuccess(users ? cmd) {
              case ReportNode.SaveSucceed ⇒
                complete(ok())
              case ReportNode.SaveFailure(t) ⇒
                complete(error(t))
            }
          }
        }
      }
    } ~ (delete & path(Segment)) { id ⇒
      userFromSession { user ⇒
        val reportId = UUID.fromString(id)
        val cmd = ReportNode.Clear(user.login, reportId)
        onSuccess(users ? cmd) {
          case ReportNode.DeleteSucceed ⇒
            complete(ok())
          case ReportNode.DeleteFailure(t) ⇒
            complete(error(t))
        }
      }
    }
  }
}
