package com.abiao.sina.service.impl;

import com.abiao.sina.dao.MonitorContentMapper;
import com.abiao.sina.entity.MonitorContent;
import com.abiao.sina.service.MonitorContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorContentServiceImpl implements MonitorContentService {

    @Autowired
    private MonitorContentMapper monitorContentMapper;

    @Override
    public void insert(List<MonitorContent> monitorContentList) {
        monitorContentMapper.insert(monitorContentList);
    }

    @Override
    public List<MonitorContent> query() {
        return monitorContentMapper.query();
    }
}
