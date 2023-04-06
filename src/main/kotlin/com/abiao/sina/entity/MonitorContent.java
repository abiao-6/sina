package com.abiao.sina.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MonitorContent {
    private String id;
    private String userUid;
    private String weiboContent;
    private Date weiboDate;
}
