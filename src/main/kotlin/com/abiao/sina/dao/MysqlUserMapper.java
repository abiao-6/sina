package com.abiao.sina.dao;

import com.abiao.sina.entity.MysqlUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MysqlUserMapper {

    void insert(List<MysqlUser> mysqlUserList);

    List<MysqlUser> queryFolloweds(String userUid);

    MysqlUser getFollowedByNameOrId(MysqlUser user);

    void updateIsSpider(@Param("followedIds") List<String> followedIds);

    void updateFollowedName(List<MysqlUser> nullFollowedNames);

    void initIsMonitored(String userUid);

    List<MysqlUser> getFollowedNameByIds(List<String> followedIds);

    List<MysqlUser> queryMonitored(String userUid);
}
