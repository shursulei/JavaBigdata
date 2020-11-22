import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Test {
    public static void main(String[] args) {
        ActorSystem a=ActorSystem.create("sys");
        ActorRef actorRef=a.actorOf(Props.create(Actordemo.class),"actorDemo");
        System.out.print("test");
    }
}
