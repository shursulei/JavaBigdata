package com.shursulei.test;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import com.typesafe.config.Config;


public class MsgPriorityMailBox extends UnboundedStablePriorityMailbox {

    public MsgPriorityMailBox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            /**
             * 返回值越小，优先级越高
             * @param message
             * @return
             */
            @Override
            public int gen(Object message) {
                if(message.equals("张三")){
                    return 0;
                }else if(message.equals("李四")){
                    return 1;
                }else if(message.equals("王五")){
                    return 2;
                }
                return 3;
            }
        });
    }
}
