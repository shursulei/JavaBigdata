package com.shursulei.quickstart;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

/**
 * TODO * @version 1.0 * @author shursulei * @date 2020/12/2 18:32
 */
public class Printer {
    public static class PrintMe {
        public final String message;

        public PrintMe(String message) {
            this.message = message;
        }
    }
    public static Behavior<PrintMe> create() {
        return Behaviors.setup(
                context ->
                        Behaviors.receive(PrintMe.class)
                                .onMessage(
                                        PrintMe.class,
                                        printMe -> {
                                            context.getLog().info(printMe.message);
                                            return Behaviors.same();
                                        })
                                .build());
    }
}

class PrinterMain{
    public static void main(String[] args) {
        final ActorSystem<Printer.PrintMe> system =
                ActorSystem.create(Printer.create(), "printer-sample-system");

// note that system is also the ActorRef to the guardian actor
        final ActorRef<Printer.PrintMe> ref = system;

// these are all fire and forget
        ref.tell(new Printer.PrintMe("message 1"));
        ref.tell(new Printer.PrintMe("message 2"));
    }
}
