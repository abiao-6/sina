package com.abiao.sina.job;

import com.abiao.sina.service.ProxyService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author abiao
 */
@Component
public class ProxyCheckEffectivelyJob {
    @Resource
    private ProxyService proxyService;

    @XxlJob("ProxyCheckEffectivelyHandler")
    public ReturnT<String> proxyCheckEffectivelyHandler(String param) {
        proxyService.checkEffectively();
        return ReturnT.SUCCESS;
    }
}
