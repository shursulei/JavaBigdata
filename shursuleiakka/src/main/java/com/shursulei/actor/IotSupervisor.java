package com.shursulei.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class IotSupervisor extends AbstractActor {
    //log
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    //内部创建props
    public static Props props() {
        return Props.create(IotSupervisor.class, IotSupervisor::new);
    }
    @Override
    public void preStart() {
        log.info("IoT Application started");
    }

    @Override
    public void postStop() {
        log.info("IoT Application stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .build();
    }
}
