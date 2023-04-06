package com.abiao.sina.entity.Bot;

import lombok.Data;

@Data
public class InformationTemplate {
    private String msg;
    private String toUserUid;

    public InformationTemplate() {
    }

    public InformationTemplate(String msg, String toUserUid) {
        this.msg = msg;
        this.toUserUid = toUserUid;
    }
}
