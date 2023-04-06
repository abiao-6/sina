package com.abiao.sina.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.abiao.sina.common.utils.HttpUtils;
import com.abiao.sina.constant.BotAccessPath;
import com.abiao.sina.entity.Bot.InformationTemplate;
import com.abiao.sina.feign.entity.RealTimeKeyword;
import com.abiao.sina.service.WxBotService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxBotServiceImpl implements WxBotService {



    @Override
    public void sendMsg(String msg, List<String> toWxid) {
        toWxid.forEach(wxid -> createSendMsgPost(msg,wxid));

    }

    @Override
    public List<RealTimeKeyword> queryRealTimeKeyword() {
        RealTimeKeyword realTimeKeyword = new RealTimeKeyword();
        String str = JSONUtil.toJsonStr(realTimeKeyword);
        HttpResponse execute = HttpUtil.createPost(BotAccessPath.SINA_QUERY_REALTIME_KEYWORD)
                .body(str)
                .execute();
        String body = execute.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        return jsonObject.getBeanList("data", RealTimeKeyword.class);
    }

    private void createSendMsgPost(String msg, String toWxid){
        InformationTemplate informationTemplate = new InformationTemplate(msg,toWxid);
        String body = JSONUtil.toJsonStr(informationTemplate);

        HttpRequest.post(BotAccessPath.SINA_QUERY_REALTIME_KEYWORD)
                .addHeaders(HttpUtils.getHeader())
                .body(body)
                .execute();
    }
}
