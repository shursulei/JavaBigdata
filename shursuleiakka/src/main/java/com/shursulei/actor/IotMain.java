package com.shursulei.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;


public class IotMain {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("iot-system");
        System.out.println(system.settings());
        try {
            ActorRef supervisor = system.actorOf(IotSupervisor.props(), "iot-supervisor");
            System.out.println("Press ENTER to exit the system");
            System.in.read();
        } finally  {
            system.terminate();
        }
    }
}
