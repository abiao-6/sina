package com.abiao.sina.entity;

import lombok.Data;

@Data
public class Proxy {
    private String ip;
    private int post;

    public Proxy(String ip,int post){
        this.ip = ip;
        this.post = post;
    }

    public Proxy(){
    }
}
