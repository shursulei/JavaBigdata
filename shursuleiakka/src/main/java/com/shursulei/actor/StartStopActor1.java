package com.shursulei.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class StartStopActor1 extends AbstractActor {
    static Props props() {
        return Props.create(StartStopActor1.class, StartStopActor1::new);
    }
    @Override
    public void preStart() {
        System.out.println("first started");
        getContext().actorOf(StartStopActor2.props(), "second");
    }

    @Override
    public void postStop() {
        System.out.println("first stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("stop",p->{getContext().stop(getSelf());}).build();
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("shursuleisystem");
        ActorRef first = system.actorOf(StartStopActor1.props(), "first");
        //发送消息给第一个actor
        first.tell("stop", ActorRef.noSender());
        //停止进程
        system.terminate();
    }
}
