package com.abiao.sina.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class NotificationDrawJob {

    @XxlJob("NotificationDrawJob")
    public ReturnT<String> NotificationDrawJob(String param) {
        System.out.println("开始监控 " );
        return ReturnT.SUCCESS;
    }
}
