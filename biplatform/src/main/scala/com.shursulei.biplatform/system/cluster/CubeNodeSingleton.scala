package com.shursulei.biplatform.system.cluster

import akka.actor.{ActorSystem, PoisonPill}
import akka.cluster.singleton._
import com.shursulei.biplatform.system.CubeNode

/**
 * @author shursulei
 */
trait CubeNodeSingleton {
  val system: ActorSystem

  val singletonManager = system.actorOf(
    ClusterSingletonManager.props(
      CubeNode.props,
      PoisonPill,
      ClusterSingletonManagerSettings(system)
        .withRole(Roles.designer)
        .withSingletonName(CubeNode.name)
    )
  )

  val cubeNode = system.actorOf(
    ClusterSingletonProxy.props(
      singletonManager.path.child(CubeNode.name)
      .toStringWithoutAddress,
      ClusterSingletonProxySettings(system)
        .withRole(Roles.designer)
        .withSingletonName("cube-proxy")
    )
  )
}
