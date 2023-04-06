package com.abiao.sina.dao;

import com.abiao.sina.entity.MonitorContent;

import java.util.List;

public interface MonitorContentMapper {
    void insert(List<MonitorContent> monitorContentList);

    List<MonitorContent> query();
}
