package com.abiao.sina.service;

import com.abiao.sina.feign.entity.RealTimeKeyword;

import java.util.ArrayList;
import java.util.List;

public interface WxBotService {

    /**
     * 发送微信消息
     * @param msg 消息
     * @param toWxid 接收人集合
     */
    void sendMsg(String msg, List<String> toWxid);

    /**
     * 发送微信消息
     * @param msg 消息
     * @param toWxId 接收人
     */
    default void sendMsg(String msg, String toWxId){
        List<String> toWxIdList = new ArrayList<>();
        toWxIdList.add(toWxId);
        sendMsg(msg,toWxIdList);
    }

    /**
     * 获取所有微博实时监控关键字
     * @return
     */
    List<RealTimeKeyword> queryRealTimeKeyword();
}
