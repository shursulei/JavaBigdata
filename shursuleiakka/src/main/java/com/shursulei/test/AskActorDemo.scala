package com.shursulei.test

import akka.actor.{AbstractActor, ActorRef}

class AskActorDemo extends AbstractActor{
  override def getSender(): ActorRef = super.getSender()
  override def createReceive(): AbstractActor.Receive = ???
}
