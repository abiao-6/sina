package com.abiao.sina.service;

import com.abiao.sina.entity.CloudPackageStoreWeiBo;
import com.abiao.sina.entity.MysqlUser;
import com.abiao.sina.feign.entity.RealTimeKeyword;

import java.util.List;

public interface UserService {
    List<MysqlUser> get(String userUid);

    void refresh();

    MysqlUser getFollowed(MysqlUser user);

    void replaceFollowed(MysqlUser mysqlUser);

    void execSpider();

    void yunBaoChang();

    List<StringBuilder> search(RealTimeKeyword timeKeyword);
}
