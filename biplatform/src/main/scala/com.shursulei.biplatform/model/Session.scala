package com.shursulei.biplatform.model

import com.softwaremill.session.{MultiValueSessionSerializer, SessionSerializer}

import scala.util.Try

/**
 * @author souo
 */
case class Session(user: User)

object Session {
  implicit val serializer: SessionSerializer[Session, String] = new MultiValueSessionSerializer[Session](
    (t: Session) ⇒ Map("login" → t.user.login),
    (m) ⇒ Try {
      Session(User(m("login")))
    }
  )
}