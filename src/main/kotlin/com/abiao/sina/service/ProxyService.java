package com.abiao.sina.service;

import com.abiao.sina.entity.Proxy;

public interface ProxyService {

    /**
     * 随机获取一个高可用代理IP
     * @return proxy代理对象
     */
    Proxy get();

    /**
     * 使用xxl-job定时校验代理的可用性
     */
    void checkEffectively();
}
