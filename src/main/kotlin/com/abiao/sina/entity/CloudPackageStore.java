package com.abiao.sina.entity;

import lombok.Data;

import java.util.List;

@Data
public class CloudPackageStore {
    private List<CloudPackageStoreWeiBo> cards;
    private String scheme;
    private int showAppTips;
}
