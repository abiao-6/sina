package com.abiao.sina.job;

import com.abiao.sina.service.UserService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitorCloudPackageStoreJob {

    @Autowired
    private UserService userService;

    @XxlJob("CloudPackageStoreJobHandler")
    public ReturnT<String> cloudPackageStoreJobHandler(String param) {
        System.out.println("开始云包场监控 ");
        userService.yunBaoChang();
        return ReturnT.SUCCESS;
    }
}
