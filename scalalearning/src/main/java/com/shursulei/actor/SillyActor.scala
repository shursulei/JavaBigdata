package com.shursulei.actor

import akka.actor.Actor

object SillyActor extends Actor{
  override def preStart(): Unit = println("I'm acting")
  override def receive: Receive = ???
  def act(): Unit ={
    for (i<- 1 to 5){
      println("I'm acting")
      Thread.sleep(1000)
    }
  }

  def main(args: Array[String]): Unit = {
    SillyActor.preStart()
  }
}
