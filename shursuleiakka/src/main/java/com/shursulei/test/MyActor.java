package com.shursulei.test;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActor extends AbstractActor {
    private final LoggingAdapter log= Logging.getLogger(getContext().getSystem(),this);

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class,s->{
            log.info("Received String message:{}",s);
        }).matchAny(o->log.info("received unknown message")).build();
    }

    Props props1 = Props.create(MyActor.class);
    //常用工厂类的方法
    static class DemoActor extends AbstractActor {
        @Override
        public Receive createReceive() {
            return null;
        }
    }
}
