package com.shursulei.biplatform.services.ds

import akka.actor.ActorRef
import akka.http.scaladsl.server.AuthorizationFailedRejection
import com.shursulei.biplatform.common.api.{DefaultTimeOut, RoutesSupport, SessionSupport}
import com.typesafe.scalalogging.StrictLogging
import akka.http.scaladsl.server.Directives._
import com.shursulei.biplatform.common.api.Response._
import com.shursulei.biplatform.system.DataSourceNode
import akka.pattern.ask
import com.shursulei.biplatform.model.{DataSources, TableInfo}
import io.circe.generic.auto._
import java.util.UUID

import com.shursulei.biplatform.services.user.UserService
import com.shursulei.biplatform.system.DataSourceNode.{Item, Items}
import com.shursulei.biplatform.model._
import io.circe.Encoder

/**
 * @author shursulei
 */
trait DataSourceRoutes extends RoutesSupport with StrictLogging with SessionSupport with DefaultTimeOut {

  val userService: UserService
  val dsNode: ActorRef

  implicit val encoderItem: Encoder[Item] = {
    Encoder.forProduct2("dsId", "name") { u ⇒
      (u.dsId, u.name)
    }
  }

  val dsRoutes = pathPrefix("datasource") {
    listAllDS ~
      addMySqlSource ~
      addH2Source ~
      listAllTables ~
      listAllColumns ~
      removeDs
  }

  //获取定义的所有 datasource
  val listAllDS = {
    (get & pathEnd) {
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            val cmd = DataSourceNode.ListAllDS
            onSuccess((dsNode ? cmd).mapTo[DataSourceNode.Items]) { res ⇒
              complete(ok[Items](res))
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    }
  }

  val addMySqlSource = {
    (post & path("mysql")) {
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            entity(as[RequestParams.Mysql]) {
              case RequestParams.Mysql(name, host, port, db, user, pwd) ⇒
                val ds = DataSources.mysql(host, port, db, user, pwd)
                val cmd = DataSourceNode.Add(name, ds)
                onSuccess((dsNode ? cmd).mapTo[DataSourceNode.Item]) { res ⇒
                  complete(ok(res))
                }
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    }
  }

  val addH2Source = {
    (post & path("h2")) {
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            entity(as[RequestParams.H2]) { h2Config ⇒
              val ds = DataSources.h2(h2Config.url, h2Config.user.mkString, h2Config.pwd.mkString)
              val cmd = DataSourceNode.Add(h2Config.name, ds)
              onSuccess((dsNode ? cmd).mapTo[DataSourceNode.Item]) { res ⇒
                complete(ok(res))
              }
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    }
  }

  val listAllTables = {
    (get & path(Segment)) { id ⇒
      val dsId = UUID.fromString(id)
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            val cmd = DataSourceNode.ListAllTables(dsId)
            onSuccess((dsNode ? cmd).mapTo[Result[List[TableInfo]]]) {
              case Left(t) ⇒
                complete(error(t))
              case Right(r) ⇒
                complete(ok(r))
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    }
  }

  val listAllColumns = {
    (get & path(Segment / Segment)) { (did, tid) ⇒
      val dsId = UUID.fromString(did)
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            val cmd = DataSourceNode.ListAllColumns(dsId, tid)
            onSuccess((dsNode ? cmd).mapTo[Result[List[ColumnInfo]]]) {
              case Left(t) ⇒
                complete(error(t))
              case Right(r) ⇒
                complete(ok(r))
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    }
  }

  val removeDs = {
    (delete & path(Segment)) { id ⇒
      val dsId = UUID.fromString(id)
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            val cmd = DataSourceNode.Remove(dsId)
            onSuccess((dsNode ? cmd).mapTo[Result[_]]) {
              case Left(t) ⇒
                complete(error(t))
              case Right(_) ⇒
                complete(ok())
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    }
  }

}
