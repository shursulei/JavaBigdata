package com.shursulei.biplatform.services.user

import akka.http.scaladsl.server.Directives._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.typesafe.scalalogging.StrictLogging
import io.swagger.annotations._
import javax.ws.rs.Path

import com.shursulei.biplatform.common.api.{Ok, RoutesSupport, SessionSupport}
import com.shursulei.biplatform.common.api.Response._
import com.shursulei.biplatform.model.{Session, User}

import io.circe.generic.auto._

/**
 * @author shursulei
 */
@Api(value = "/api/users", tags = Array("user"), produces = "application/json")
@Path("/api/users")
trait UsersRouters extends RoutesSupport with StrictLogging with SessionSupport {

  val userService: UserService

  val usersRoutes = pathPrefix("users") {
    login ~ logout
  }

  @Path("/login")
  @ApiOperation(
    value           = "登入",
    httpMethod      = "POST",
    consumes        = "application/json",
    responseHeaders = Array(
      new ResponseHeader(name = "Set-Cookie", description = "set an cookie")
    )
  )
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(
        value     = "",
        paramType = "body",
        dataType  = "string",
        required  = true,
        example   = "{\"login\":\"admin\"}"
      )
    )
  )
  @ApiResponses(
    Array(
      new ApiResponse(
        code     = 200,
        message  = "成功",
        response = classOf[Ok[Map[String, Any]]]
      ),
      new ApiResponse(
        code     = 500,
        message  = "失败",
        response = classOf[Error]
      )
    )
  )
  def login = path("login") {
    post {
      entity(as[User]) { user ⇒
        onSuccess(userService.isAdminUser(user)) { isAdmin ⇒
          val rule = if (isAdmin) "admin"; else "normal"
          logger.info(s"${user.login} login as $rule")
          val session = Session(user)
          setSession(oneOff, usingCookies, session) {
            complete(ok(Map("isAdmin" → isAdmin)))
          }
        }
      }
    }
  }

  @Path("/logout")
  @ApiOperation(value = "登出", httpMethod = "GET")
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(
        name      = "Cookie",
        paramType = "header",
        required  = true,
        dataType  = "string",
        example   = "_sessiondata=1624203CB361BCD4E2FF55E9C01981085260DBFC" +
        "-9CDDCC6DFBF3F232B48DBB88C5C29E16EC6F2E7AD40615429E8DB805CEDE8549CD72FA78921F5E7A4F15B56F7DBBD6B6;" +
        " Path=/; HttpOnly"
      )
    )
  )
  def logout = path("logout") {
    get {
      userFromSession { user ⇒
        logger.info(s"${user.login} logout")
        invalidateSession(oneOff, usingCookies) {
          complete(ok())
        }
      }
    }
  }
}
