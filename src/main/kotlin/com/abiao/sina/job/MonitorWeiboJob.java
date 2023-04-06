package com.abiao.sina.job;

import com.abiao.sina.service.UserService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorWeiboJob {

    @Autowired
    private UserService userService;

    @XxlJob("monitorWeiboJobHandler")
    public ReturnT<String> monitorWeiboJobHandler(String param) {
        System.out.println("开始监控 ");
        userService.execSpider();
        return ReturnT.SUCCESS;
    }
}
