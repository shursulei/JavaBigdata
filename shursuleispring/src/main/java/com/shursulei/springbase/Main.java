package com.shursulei.springbase;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void  main(String args[]){
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(DigConfig.class);
        UseFunctionService useFunctionService=context.getBean(UseFunctionService.class);
        System.out.println(useFunctionService.sayHello("d1"));
        context.close();
    }
}
