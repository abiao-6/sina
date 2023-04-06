package com.abiao.sina.controller;

import com.abiao.sina.entity.Proxy;
import com.abiao.sina.service.ProxyService;
import com.abiao.web.infrastructure.BaseApiController;
import com.abiao.web.infrastructure.model.JsonResultMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/proxy")
public class ProxyController extends BaseApiController {

    @Resource
    private ProxyService proxyService;

    @GetMapping
    public JsonResultMessage get() {
        Proxy proxy = proxyService.get();
        return success(proxy);
    }
}
