package com.shursulei.springbase.event;

import com.shursulei.springbase.DigConfig;
import com.shursulei.springbase.UseFunctionService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void  main(String args[]){
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(EventConfig.class);
        DemoPublisher demopublisher=context.getBean(DemoPublisher.class);
        demopublisher.publish("hello application event");
        context.close();
    }
}
