package com.abiao.sina.job;

import com.abiao.sina.service.PluginService;
import com.abiao.sina.service.ProxyService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author abiao
 */
@Component
public class PluginJob {
    @Resource
    private PluginService pluginService;

    @XxlJob("PluginHandler")
    public ReturnT<String> PluginHandler(String param) {
        pluginService.sZTC();
        return ReturnT.SUCCESS;
    }
}
