package com.shursulei.springbase.aware;

import com.shursulei.springbase.event.EventConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void  main(String args[]){
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(AwareConfig.class);
        AwareService awareService=context.getBean(AwareService.class);
        awareService.outputResult();
        context.close();
    }
}
