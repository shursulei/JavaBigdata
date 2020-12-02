package com.shursulei.biplatform.system

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.shursulei.biplatform.HttpService

/**
 * @author shursulei
 */
object LocalMain extends HttpService {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("designer", ConfigFactory.load("local"))

    val cubeNode = system.actorOf(CubeNode.props, CubeNode.name)

    val dsNode = system.actorOf(DataSourceNode.props, DataSourceNode.name)

    val queryRouteNode = system.actorOf(QueryRouteNode.props(cubeNode, dsNode))

    val users = system.actorOf(LocalUsers.props(queryRouteNode), LocalUsers.name)

    startService(cubeNode, users, dsNode)
  }

}
