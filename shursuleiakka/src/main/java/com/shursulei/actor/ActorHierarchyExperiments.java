package com.shursulei.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

public class ActorHierarchyExperiments {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("testSystem");

        ActorRef firstRef = system.actorOf(PrintMyActorRefActor.props(), "first-actor");
        System.out.println("First: " + firstRef);
        firstRef.tell("printit", ActorRef.noSender());

        System.out.println(">>> Press ENTER to exit <<<");
        try {
            System.in.read();
        } finally {
            system.terminate();
        }
    }
}
