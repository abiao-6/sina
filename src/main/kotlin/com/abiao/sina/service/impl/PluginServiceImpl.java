package com.abiao.sina.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abiao.sina.service.PluginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class PluginServiceImpl implements PluginService {

    @Override
    public void sZTC() {
        List<String> tokens = new ArrayList<>();
        tokens.add("9a098eaa-93e8-4d16-9dc6-f824aa16c964");
        tokens.add("4dec9294-c241-4d62-8e6d-4a8a6cbd81ac");
        tokens.add("f6385970-fe5f-4edb-ab87-0ac52e0f7983");
        var url = "https://tcyjf.szticai.com/api/user/sign";
        var luckDrawUrl = "https://tcyjf.szticai.com/api/lottery/lottery";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        headers.put("token","9a098eaa-93e8-4d16-9dc6-f824aa16c964");
        headers.put("content-type","application/json");
        for (String token : tokens) {
            headers.put("token",token);
            HttpResponse signResponse = HttpRequest.post(url)
                    .addHeaders(headers)
                    .body("{}")
                    .execute();
            log.info("签到："+signResponse.body());
            for (int i = 0; i < 3; i++) {
                HttpResponse luckDrawResponse = HttpRequest.post(luckDrawUrl)
                        .addHeaders(headers)
                        .body("{}")
                        .execute();
                log.info("抽奖："+luckDrawResponse.body());
            }
        }
    }
}
