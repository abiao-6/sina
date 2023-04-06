package com.abiao.sina.handler;

import com.abiao.sina.common.utils.CallShell;

import java.io.IOException;
import java.util.TimerTask;

/**
 * @author abiao
 */
public class SpiderTask extends TimerTask {

    private static final String SHELL_PATH = "/root/release/pythonPro/weiboSpider/spider.sh";
    private static final String SPIDER_NAME = "";

    @Override
    public void run() {
        try {
            String[] cdCall = {"sh",SHELL_PATH};
            String[] execCall = {"python",SPIDER_NAME};
//            String[] call = cdCall +" && "+ execCall;
            CallShell.call(cdCall);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
