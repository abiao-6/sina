package com.abiao.sina.controller;

import com.abiao.sina.entity.MysqlUser;
import com.abiao.sina.feign.entity.RealTimeKeyword;
import com.abiao.sina.service.UserService;
import com.abiao.web.infrastructure.BaseApiController;
import com.abiao.web.infrastructure.model.JsonResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigController extends BaseApiController {

    @Autowired
    private UserService userService;


    @GetMapping
    public JsonResultMessage get(String userUid) {
        List<MysqlUser> users = userService.get(userUid);
        return success(users);
    }

    @GetMapping("/refresh")
    public JsonResultMessage refresh() {
        userService.refresh();
        return success();
    }

    @PostMapping
    public JsonResultMessage query(@RequestBody MysqlUser user){
        userService.getFollowed(user);
        return success();
    }

    @PostMapping("/replace")
    public JsonResultMessage replaceFollowed(@RequestBody MysqlUser mysqlUser){
        userService.replaceFollowed(mysqlUser);
        return success();
    }

    @GetMapping("/execSpider")
    public JsonResultMessage execSpider(){
        userService.execSpider();
        return success();
    }

    @GetMapping("/search")
    public JsonResultMessage search(RealTimeKeyword timeKeyword){
        List<StringBuilder> weiBoList = userService.search(timeKeyword);
        return success(weiBoList);
    }

    @GetMapping("/test")
    public JsonResultMessage test(){
        userService.yunBaoChang();
        return success();
    }

}


