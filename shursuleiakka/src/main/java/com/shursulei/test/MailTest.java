package com.shursulei.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MailTest {
    public static void main(String[] args) {
        ActorSystem system=ActorSystem.create("system");
        ActorRef ref=system.actorOf(Props.create(MsgPriorityMailBox.class).withMailbox("msgprio-mailbox"),"priorityActor");
        Object[] messages={"王五","李四","张三","小二"};
        for(Object msg:messages){
            ref.tell(msg,ActorRef.noSender());
        }

    }
}
