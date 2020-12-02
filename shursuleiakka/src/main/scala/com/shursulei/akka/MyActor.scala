package com.shursulei.akka

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

class MyActor extends Actor{
  var log=Logging(context.system,this)
  def receive: Receive = {
    case "test"=>log.info("received test")
    case _ =>log.info("received unknown message")
  }
}

object Main extends App{
  val system=ActorSystem("MySystem")
  val myActor=system.actorOf(Props[MyActor],name="myactor")
}