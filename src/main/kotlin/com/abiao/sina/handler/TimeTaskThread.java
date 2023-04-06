package com.abiao.sina.handler;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Random;
import java.util.Timer;

@Slf4j
public class TimeTaskThread {
    public static void timeTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long delay = 1000L;
                long period = 1000L * 3L;
                new Timer("定时检测").schedule(new RegularlySpiderTask(), delay, period);
            }
        }).start();
    }

    public static long getRandomDate(){
        Random random = new Random();
        val number = random.nextInt(20)+10;
        return number*1000*60;
    }
}
