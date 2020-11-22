package com.shursulei.thread;

import java.util.Timer;
import java.util.TimerTask;

public class ThreadTest {
    public static void main(String[] args) {
        Timer a = new Timer();
        TimerTask A = new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
            }
        };
        a.schedule(A,0,1000);
    }
}
