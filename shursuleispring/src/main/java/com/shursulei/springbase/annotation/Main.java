package com.shursulei.springbase.annotation;

import com.shursulei.springbase.conditional.ConditionConfig;
import com.shursulei.springbase.conditional.ListService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void  main(String args[]){
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(DemoConfig.class);
        DemoService demoService=context.getBean(DemoService.class);
        demoService.outputResult();
        context.close();
    }
}
