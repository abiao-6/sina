package com.abiao.sina.service;

import com.abiao.sina.entity.MonitorContent;

import java.util.List;

public interface MonitorContentService {
    void insert(List<MonitorContent> monitorContentList);

    List<MonitorContent> query();
}
