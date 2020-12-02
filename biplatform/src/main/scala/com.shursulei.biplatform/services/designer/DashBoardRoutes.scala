package com.shursulei.biplatform.services.designer

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import com.typesafe.scalalogging.StrictLogging
import com.shursulei.biplatform.common.api.Response._
import com.shursulei.biplatform.common.api.{DefaultTimeOut, RoutesSupport, SessionSupport}
import com.shursulei.biplatform.model.DashBoard
import com.shursulei.biplatform.system.DashBoardNode.Widget
import com.shursulei.biplatform.system.{DashBoardManager, DashBoardNode, UserNode}
import io.circe.generic.auto._

/**
 * @author shursulei
 */
trait DashBoardRoutes extends RoutesSupport with StrictLogging with SessionSupport with DefaultTimeOut {
  val users: ActorRef

  val dashBoardRoutes = pathPrefix("dashboard"){
    //list all dashboards
    (get & pathEnd) {
      userFromSession { user ⇒
        val cmd = DashBoardManager.ListAllDashBoard(user.login)
        onSuccess(users ? cmd) {
          case dashBoards: Array[DashBoard] ⇒
            complete(ok(dashBoards))
        }
      }
    } ~ (post & pathEnd){ //create an dashboard
      userFromSession { user ⇒
        entity(as[RequestParams.CreateDashBoard]) { create ⇒
          val cmd = DashBoardManager.CreateDashBoard(user.login, create.dashboardName)
          onSuccess(users ? cmd) {
            case dashBoards: Array[DashBoard] ⇒
              complete(ok(dashBoards))
          }
        }
      }
    } ~ (put & pathEnd){ //rename an dashboard
      userFromSession { user ⇒
        entity(as[RequestParams.RenameDashBoard]) { rename ⇒
          val cmd = DashBoardManager.RenameDashBoard(user.login, rename.dashBoardId, rename.dashboardName)
          onSuccess(users ? cmd) {
            case dashBoards: Array[DashBoard] ⇒
              complete(ok(dashBoards))
          }
        }
      }
    } ~ (delete & path(Segment)) { id ⇒ //delete an dashboard
      userFromSession { user ⇒
        val cmd = DashBoardManager.DropDashBoard(user.login, UUID.fromString(id))
        onSuccess(users ? cmd){
          case dashBoards: Array[DashBoard] ⇒
            complete(ok(dashBoards))
        }
      }
    } ~ (get & path(Segment)){ id ⇒ //list all widget
      userFromSession { user ⇒
        val cmd = DashBoardNode.ListAll(user.login, UUID.fromString(id))
        onSuccess(users ? cmd){
          case widgets: Array[Widget] ⇒
            complete(ok(widgets))
        }
      }
    } ~ (post & path(Segment)) { id ⇒ //add an widget
      userFromSession { user ⇒
        entity(as[RequestParams.AddWidget]){ add ⇒
          val cmd = DashBoardNode.AddWidget(
            user.login,
            UUID.fromString(id),
            add.reportId,
            add.visualizationType,
            add.size
          )
          onSuccess(users ? cmd){
            case widgets: Array[Widget] ⇒
              complete(ok(widgets))
          }
        }
      }
    } ~ (delete & path(Segment / Segment)){
      case (did, wid) ⇒ //remove an widget
        userFromSession { user ⇒
          val cmd = DashBoardNode.RemoveWidget(
            user.login,
            UUID.fromString(did),
            UUID.fromString(wid)
          )
          onSuccess(users ? cmd){
            case widgets: Array[Widget] ⇒
              complete(ok(widgets))
          }
        }
    }
  }
}
