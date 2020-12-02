package com.shursulei.biplatform.common.api

import akka.http.scaladsl.server.Directives.{provide, reject}
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive1}
import com.softwaremill.session.SessionDirectives.session
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionOptions.{oneOff, usingCookies}
import com.shursulei.biplatform.SystemEnv
import com.shursulei.biplatform.model.{Session, User}

import scala.concurrent.ExecutionContext

/**
 * @author shursulei
 */
trait SessionSupport {

  implicit def sessionManager: SessionManager[Session]

  implicit def ec: ExecutionContext

  def userFromSession: Directive1[User] = {
    session(oneOff, usingCookies).flatMap {
      _.toOption match {
        case None ⇒
          if (SystemEnv.isTest) {
            provide(User("admin"))
          }
          else {
            reject(AuthorizationFailedRejection)
          }
        case Some(s) ⇒
          provide(s.user)
      }
    }
  }
}
