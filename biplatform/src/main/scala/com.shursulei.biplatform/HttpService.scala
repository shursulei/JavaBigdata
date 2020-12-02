package com.shursulei.biplatform

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.softwaremill.session.{SessionConfig, SessionManager}
import com.shursulei.biplatform.common.sql.DatabaseConfig
import com.shursulei.biplatform.model.Session
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext
import scala.language.reflectiveCalls
import scala.util.{Failure, Success}

/**
 * @author shursulei
 */
trait HttpService extends StrictLogging { SELF ⇒

  def startService(cube: ActorRef, _users: ActorRef, dataSource: ActorRef)(implicit _actorSystem: ActorSystem): Unit = {
    implicit val ec = _actorSystem.dispatcher
    implicit val _materializer = ActorMaterializer()
    val _config = _actorSystem.settings.config

    val modules = new DependencyWiring with Routes {
      override def system: ActorSystem = _actorSystem

      override val config: ServerConfig with DatabaseConfig = {
        new ServerConfig with DatabaseConfig {
          override def rootConfig: Config = _config
        }
      }

      override val users: ActorRef = _users
      override val cubeNode: ActorRef = cube
      override val dsNode: ActorRef = dataSource

      lazy val sessionConfig = SessionConfig.fromConfig(config.rootConfig).copy(sessionEncryptData = true)
      implicit lazy val sessionManager: SessionManager[Session] = new SessionManager[Session](sessionConfig)
      override implicit def ec: ExecutionContext = system.dispatchers.lookup("akka-http-routes-dispatcher")
    }

    logger.info(s"Server secret: ${modules.sessionConfig.serverSecret.take(3)} ...")
    modules.sqlDatabase.updateSchema()

    val host = modules.config.serverHost
    val port = modules.config.serverPort

    val startFuture = {
      Http().bindAndHandle(modules.api, host, port)
    }

    startFuture.onComplete {
      case Success(b) ⇒
        logger.info(s"Server started on $host:$port")
        sys.addShutdownHook {
          b.unbind()
          _actorSystem.terminate()
          logger.info("Server stopped")
        }
      case Failure(e) ⇒
        logger.error(s"Cannot start server on $host:$port", e)
        sys.addShutdownHook {
          _actorSystem.terminate()
          logger.info("Server stopped")
        }
    }
  }

}
