package com.abiao.sina.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MysqlUser {
    private String id;
    private String userUid;
    private String followedId;
    private String[] followedIds;
    private String followedName;
    private boolean isMonitored;
    private String creator;
    private Date dateCreated;
    private String updatedBy;
    private Date dateUpdated;
}



