package com.shursulei.biplatform.services.cube

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.server.AuthorizationFailedRejection
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import com.shursulei.biplatform.common.api.{DefaultTimeOut, RoutesSupport, SessionSupport}
import com.shursulei.biplatform.services.user.UserService
import com.shursulei.biplatform.system.CubeNode
import com.typesafe.scalalogging.StrictLogging
import io.circe.generic.auto._
import com.shursulei.biplatform.common.api.Response._
import com.shursulei.biplatform.model.{CubeMeta, CubeSchema}

/**
 * @author shursulei
 */
trait CubeRoutes extends RoutesSupport with StrictLogging with SessionSupport with DefaultTimeOut {

  val userService: UserService
  val cubeNode: ActorRef

  val cubeRoutes = pathPrefix("cubes") {
    (get & pathEnd){
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            val cmd = CubeNode.ListAllCube
            onSuccess((cubeNode ? cmd).mapTo[Seq[CubeMeta]]) { metaList ⇒
              complete(ok[Seq[CubeMeta]](metaList))
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    } ~ (post & pathEnd) {
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            entity(as[RequestParams.Add]) { add ⇒
              val cmd = CubeNode.Add(add.name, user.login, add.schema)
              onSuccess((cubeNode ? cmd).mapTo[CubeMeta]) { meta ⇒
                complete(ok[CubeMeta](meta))
              }
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    } ~ (get & path(Segment)) { id ⇒
      userFromSession { user ⇒
        onSuccess(userService.isAdminUser(user)) {
          case true ⇒
            val cmd = CubeNode.Get(UUID.fromString(id))
            onSuccess(cubeNode ? cmd) {
              case schema: CubeSchema ⇒
                complete(ok(schema))
              case error: Error ⇒
                complete(error)
            }
          case false ⇒
            logger.warn(s"login user ${user.login} is not allow access this resource")
            reject(AuthorizationFailedRejection)
        }
      }
    } ~ (put & path(Segment)) { id ⇒
      userFromSession { user ⇒
        entity(as[RequestParams.Update]) { update ⇒
          val cmd = CubeNode.UpdateCube(UUID.fromString(id), update.name, user.login, update.schema)
          onSuccess(cubeNode ? cmd) {
            case error: Error ⇒
              complete(error)
            case meta: CubeMeta ⇒
              complete(ok(meta))
          }
        }
      }
    } ~ (delete & path(Segment)) { cubeId ⇒
      userFromSession { user ⇒
        val cmd = CubeNode.RemoveCube(UUID.fromString(cubeId), user.login)
        onSuccess(cubeNode ? cmd) {
          case error: Error ⇒
            complete(error)
          case _ ⇒
            complete(ok())
        }
      }
    }
  }
}
