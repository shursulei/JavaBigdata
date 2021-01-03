package com.shursulei.springbase.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DemoListener implements ApplicationListener<DemoEvent> {
    @Override
    public void onApplicationEvent(DemoEvent event) {
        String msg=event.getMsg();
        System.out.println("demolistener接受到publish的消息"+msg);
    }
}
