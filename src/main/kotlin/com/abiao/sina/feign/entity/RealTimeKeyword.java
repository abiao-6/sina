package com.abiao.sina.feign.entity;

import lombok.Data;

import java.util.List;

@Data
public class RealTimeKeyword {
    private String id;
    private String userUid;
    private String userName;
    private String keyword;
    private Boolean isUse;
    private List data;

    private boolean isSearch;

    public RealTimeKeyword(){

    }

    public RealTimeKeyword(String userUid){
        this.userUid = userUid;
    }
}
