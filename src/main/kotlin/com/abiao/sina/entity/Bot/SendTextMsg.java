package com.abiao.sina.entity.Bot;

import lombok.Data;

@Data
public class SendTextMsg extends Bot{
    private String msg;

    public SendTextMsg(String msg, String toWxid) {
        this.msg = msg;
        super.setApi("SendTextMsg");
        super.setTo_wxid(toWxid);
    }

}
