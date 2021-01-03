package com.shursulei.quickstart;

import akka.actor.typed.ActorSystem;

public class HelloWorldMaintest{
    public static void main(String[] args) {
        final ActorSystem<HelloWorldMain.SayHello> system =
                ActorSystem.create(HelloWorldMain.create(), "hello");
//        System.out.println(system.settings());
        system.tell(new HelloWorldMain.SayHello("World"));
//        system.tell(new HelloWorldMain.SayHello("Akka"));
    }
}
