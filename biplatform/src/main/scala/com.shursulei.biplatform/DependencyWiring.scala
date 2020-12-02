package com.shursulei.biplatform

import akka.actor.ActorSystem
import com.shursulei.biplatform.common.sql.{DatabaseConfig, SqlDatabase}
import com.shursulei.biplatform.services.user.{UserRuleDao, UserService}
import com.typesafe.scalalogging.StrictLogging

/**
 * @author shursulei
 */
trait DependencyWiring extends StrictLogging {
  def system: ActorSystem
  val config: ServerConfig with DatabaseConfig
  lazy val daoExecutionContext = system.dispatchers.lookup("dao-dispatcher")
  lazy val sqlDatabase = SqlDatabase.create(config)
  lazy val userRuleDao = new UserRuleDao(sqlDatabase)(daoExecutionContext)
  lazy val userService = new UserService(userRuleDao)(daoExecutionContext)
}
