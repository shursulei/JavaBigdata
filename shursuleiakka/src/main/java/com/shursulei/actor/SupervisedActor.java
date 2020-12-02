package com.shursulei.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class SupervisedActor extends AbstractActor {
    static Props props() {
        return Props.create(SupervisedActor.class, SupervisedActor::new);
    }

    @Override
    public void preStart() {
        System.out.println("supervised actor prestarted");
    }

    @Override
    public void postStop() {
        System.out.println("supervised actor prestopped");
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("fail", f -> {
                    System.out.println("supervised actor fails now");
                    throw new Exception("I failed!");
                })
                .build();
    }
}
