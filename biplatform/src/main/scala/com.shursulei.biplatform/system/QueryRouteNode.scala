package com.shursulei.biplatform.system

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import com.shursulei.biplatform.model.{CubeSchema, DataSource, MySqlSource, ReportQuery}
import akka.pattern.ask
import com.shursulei.biplatform.common.api.{DefaultTimeOut, Response}
import Response.error
import com.shursulei.biplatform.queryrouter.jdbc.JDBCEngine
import com.shursulei.biplatform.system.DataSourceNode.Item

import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future

/**
 * @author shursulei
 */
class QueryRouteNode(cube: ActorRef, dataSource: ActorRef) extends Actor with DefaultTimeOut {

  import context.dispatcher

  override def receive: Receive = {
    case query: ReportQuery ⇒
      val replyTo = sender()
      execute(query)(replyTo)

    case _ ⇒

  }

  def execute(query: ReportQuery)(replyTo: ActorRef) = {
    (cube ? CubeNode.Get(query.cubeId)).onComplete{
      case Success(schema: CubeSchema) ⇒
        getDataSource(schema.dataSourceId).onComplete{
          case Success(Some(ds: DataSource)) ⇒
            ds match {
              case mysql: MySqlSource ⇒
                val engine = new JDBCEngine(mysql)
                val source = engine.execute(query.queryModel)
                replyTo ! source
            }
          case Success(None) ⇒
            replyTo ! error(s"no such ds ${schema.dataSourceId}")
          case Failure(t) ⇒
            replyTo ! error(t)
        }

      case Success(msg: Response) ⇒
        replyTo ! msg

      case Success(_) ⇒

      case Failure(t) ⇒
        replyTo ! error(t)
    }

  }

  def getDataSource(id: UUID): Future[Option[DataSource]] = {
    (dataSource ? DataSourceNode.Get(id)).mapTo[Option[Item]].map{
      _.map(_.dataSource)
    }
  }

}

object QueryRouteNode {

  def props(cube: ActorRef, dataSource: ActorRef) = Props(classOf[QueryRouteNode], cube, dataSource)

  val name = "query-route"
}
