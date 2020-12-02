package com.shursulei.quickstart;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.PostStop;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * TODO * @version 1.0 * @author shursulei * @date 2020/12/2 10:42
 * 启动和停止actor
 */
public class StartStopActor1 extends AbstractBehavior<String> {
    static Behavior<String> create() {
        return Behaviors.setup(StartStopActor1::new);
    }
    private StartStopActor1(ActorContext<String> context) {
        super(context);
        System.out.println("first started");
        context.spawn(StartStopActor2.create(), "second");
    }
    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("stop", Behaviors::stopped)
                .onSignal(PostStop.class, signal -> onPostStop())
                .build();
    }
    private Behavior<String> onPostStop() {
        System.out.println("first stopped");
        return this;
    }
}
class StartStopActor2 extends AbstractBehavior<String> {

    static Behavior<String> create() {
        return Behaviors.setup(StartStopActor2::new);
    }

    private StartStopActor2(ActorContext<String> context) {
        super(context);
        System.out.println("second started");
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder().onSignal(PostStop.class, signal -> onPostStop()).build();
    }

    private Behavior<String> onPostStop() {
        System.out.println("second stopped");
        return this;
    }
}
 class starstopactormain{
     public static void main(String[] args) {
//         ActorRef<String> first = context.spawn(StartStopActor1.create(), "first");
//         first.tell("stop");
     }
 }