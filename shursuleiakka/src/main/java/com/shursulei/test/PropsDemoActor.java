package com.shursulei.test;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Creator;

/**
 *
 */
public class PropsDemoActor extends AbstractActor {
    /**
     * 工厂类
     * @return props
     */
    public static Props createProps(){
        //实现Creator接口并传入Props.create方法
        return Props.create(new Creator<PropsDemoActor>() {
            @Override
            public PropsDemoActor create() throws Exception {
                //创建Actor
                return new PropsDemoActor();
            }
        });
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
