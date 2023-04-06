package com.abiao.sina.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.abiao.sina.common.utils.FutureUtil;
import com.abiao.sina.entity.Proxy;
import com.abiao.sina.service.ProxyService;
import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class ProxyServiceImpl implements ProxyService {

    volatile private static List<Proxy> threadUsedIp = new ArrayList<>();
    volatile private static List<String> proxies = new ArrayList<>(500);

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static RedisClient redisClient = RedisClient.create("redis://:y1475369REDIS@openaddr.cn:2000");

    @Override
    public void checkEffectively() {
        getProxies();
        String requestUrl = "http://www.baidu.com";
        var size = threadUsedIp.size();
        var delList = new ArrayList<Proxy>();
        var futures = new ArrayList<CompletableFuture<Void>>();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> test(finalI, requestUrl, delList), executorService);
            futures.add(future);
        }
        FutureUtil.allOf(futures);
        threadUsedIp.removeAll(delList);
        log.info("定时检测中 threadUsedIp.size ：" + threadUsedIp.size());
        log.info("定时检测中 proxies.size ：" + proxies.size());
    }

    private static void test(int i, String requestUrl, List<Proxy> delList) {
        var proxy = threadUsedIp.get(i);
        var ipPost = new String[]{proxy.getIp(), String.valueOf(proxy.getPost())};
        var responseJson = getResponseJson(requestUrl, ipPost, 1000);
        if (StringUtils.isEmpty(responseJson)) {
            try {
                delList.add(proxy);
            } catch (Exception e) {
                log.error("删除下标{}的ip失败,{}", i, e.getMessage());
            }
        }
    }

    {
        getProxies();
        var executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            CompletableFuture.runAsync(ProxyServiceImpl::exec, executorService);
        }
        log.info("executorService = " + executorService);
    }

    @Override
    public Proxy get() {

        // threadUsedIp为空,批量获取高效ip
        if (threadUsedIp.size() == 0) {
            var body = HttpUtil.createGet("http://openaddr.cn:5020/random")
                    .execute()
                    .body();
            var ipPost = body.split(":");
            return new Proxy(ipPost[0], Integer.parseInt(ipPost[1]));
        }

        var num = 0;
        if (threadUsedIp.size() != 1) {
            num = RandomUtil.randomInt(0, threadUsedIp.size() - 1);
        }

        var proxy = threadUsedIp.get(num);
//        log.info("获取代理成功 下标index = " + num + "ip: " + proxy.getIp() + " post: " + proxy.getPost());
//        log.info("threadUsedIp.size=======" + threadUsedIp.size() + "===" + threadUsedIp.get(0));
        return proxy;
    }

    public static void getProxies() {
        //连接Redis
        val connect = redisClient.connect();
        //获取到异步连接Api
        val sync = connect.sync();
        //获取到评分为80-100分数的代理
        proxies = sync.zrevrangebyscore("proxies:weibo", Range.unbounded(), Limit.from(100));
        connect.close();
    }

    private static void exec() {
        while (true) {
            sendPost();
        }
    }

    private static void sendPost() {
        val requestUrl = "http://www.baidu.com";
        val index = RandomUtil.randomInt(0, proxies.size() - 1);
        var body = proxies.get(index);
        var ipPost = body.split(":");
        getResponseJson(requestUrl, ipPost, 500);
    }

    private static String getResponseJson(String url, String[] ipPost, int timeout) {
        var ip = ipPost[0];
        var post = Integer.parseInt(ipPost[1]);
        String responseJson = null;
        try {
            responseJson = HttpUtil.createGet(url)
                    .setHttpProxy(ip, post)
                    .timeout(timeout)
                    .execute()
                    .body();
            if (responseJson.contains("status: 418")) {
                return "";
            }
            saveIp(ip, post);
            return responseJson;
        } catch (Exception e) {
            return "";
        }
    }

    private static final String COOKIE = "_T_WM=4e661fef4c394d44b19b6cf2d7e45d00; __bid_n=184fcad6f42d5db25a4207; FEID=v10-264b156dcb8fba1e65afe72c66e9317ee15b71a5; __xaf_fpstarttimer__=1671518385236; __xaf_thstime__=1671518385255; FPTOKEN=FKITfFGBEyA9SJQs7ObBIljxMvMJvmzgOKKGqw45OatwohbCP/pJjnsWnU05nZAIyjnrJb62z2PxaEjDwUNouHyiakggezu9guURiKod80KHNA3u9XUxwh0dLRjvO9LZMD+DufeqglnRba33mWwSeWyXSKfG/9gnBQaKEG2ssJ4+C+AH8U0i3c5qb0gHVYTD0eEWs6zDsBduLR8ox87Q6l72rG88bjl9E6Lixwkhevom2oz7I/S8Ryj5aUkeJCNE80OpJIS1ljm5wZ3nRAnjFE/aYKARvpK0ORxybY3iPz8Q31n1d4gGNEgsAmnJJxadFFl2EBI9MEMywoHd7bsdHUY/bU78O50m56hh2RJfRZ3dzpC6VVKyeBHmd51xBCRRAPuI5cdsHB9H3GF2CdnUHA==|N0lM6zCd9czEqZ5jNUANwNIdytQAWzpj3/yYD6/8auo=|10|177d894af944d7826ff6441bf84ef6c6; __xaf_fptokentimer__=1671518385285; SUB=_2A25OrSzyDeRhGeFJ71EX9yrNzj6IHXVqUbS6rDV6PUJbkdANLUf9kW1Nf_1NJ37s8i1JSLKNF_aA7RrHH0VfpUh4; SCF=AqjRNogLdZWjgOcnMEyd5IWigWUqPB4D1jXXX7qS6K1evEUT8LrThdQXRdSj4wialygxrui5NmXyQ7-Xhi_L1LU.; SSOLoginState=1672043682";

    private static HashMap<String, String> getHeaders() {
        var headers = new HashMap<String, String>(2);
        headers.put("cookie", COOKIE);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        return headers;
    }

    synchronized private static void saveIp(String ip, int post) {
        var proxy = new Proxy();
        proxy.setIp(ip);
        proxy.setPost(post);
        if (threadUsedIp.contains(proxy)) {
            return;
        }
        threadUsedIp.add(proxy);
    }

    synchronized private static void removeIp(int index) {
        threadUsedIp.remove(index);
    }
}
