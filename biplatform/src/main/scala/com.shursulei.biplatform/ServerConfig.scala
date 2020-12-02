package com.shursulei.biplatform

import cats.syntax.either._
import com.typesafe.config.Config

/**
 * @author shursulei
 */
trait ServerConfig {

  def rootConfig: Config

  lazy val serverHost: String = Either.catchNonFatal(
    rootConfig.getString("designer.http.server.host").trim
  ).getOrElse("0.0.0.0")

  lazy val serverPort: Int = Either.catchNonFatal(
    rootConfig.getInt("designer.http.server.port")
  ).getOrElse(8080)
}
