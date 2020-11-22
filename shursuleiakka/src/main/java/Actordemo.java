import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actordemo extends UntypedActor {
    private LoggingAdapter log= Logging.getLogger(this.getContext().system(),this);
    @Override
    //instanceof 严格来说是Java中的一个双目运算符，用来测试一个对象是否为一个类的实例
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof String){
            log.info(msg.toString());
        }else {
            unhandled(msg);
        }

    }
}
