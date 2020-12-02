package com.shursulei.biplatform.system.cluster

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.shursulei.biplatform.system.UserNode

/**
 * @author shursulei
 */
trait UserNodeShard {
  val system: ActorSystem

  val queryRouteNode: ActorRef

  ClusterSharding(system).start(
    typeName        = ShardUsers.shardName,
    entityProps     = UserNode.props(queryRouteNode),
    settings        = ClusterShardingSettings(system).withRole(Roles.designer),
    extractEntityId = ShardUsers.extractEntityId,
    extractShardId  = ShardUsers.extractShardId
  )

  def shardUser: ActorRef = {
    ClusterSharding(system).shardRegion(ShardUsers.shardName)
  }
}
