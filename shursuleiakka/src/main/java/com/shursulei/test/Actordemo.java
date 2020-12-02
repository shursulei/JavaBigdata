package com.shursulei.test;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actordemo extends AbstractActor {
    private LoggingAdapter log= Logging.getLogger(this.getContext().system(),this);
    //instanceof 严格来说是Java中的一个双目运算符，用来测试一个对象是否为一个类的实例
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof String){
            log.info(msg.toString());
        }else {
            unhandled(msg);
        }

    }
    public static void main(String[] args) {
//        ActorContext childActor=getContext.acforOf(Props.create(childActor.class),"childActor");
        ActorSystem system = ActorSystem.create("sys");
//        ActorRef actorRef = a.actorOf(Props.create(Actordemo.class), "actorDemo");
        ActorRef ref=system.actorOf(Props.create(Actordemo.class).withDispatcher("my-forkjoin-dispatcher"),"ActorDemo");
//        ActorRef ref=system.actorOf(PropsDemoActor.createProps(),"propsActor");
        System.out.println(system.settings());
        System.out.println(ref.path());
        System.out.println("处理器个数:"+Runtime.getRuntime().availableProcessors());
        // noSender表示无发送者
//        ActorSelection=[ActorSystem/ActorContext ].
        ref.tell("Helo akka",ActorRef.noSender());
    }


    @Override
    public Receive createReceive() {
        return null;
    }
}
