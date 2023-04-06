package com.abiao.sina.common.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static Map<String,String> getHeader(){
        HashMap<String, String> handlers = new HashMap<>();
        handlers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");
        handlers.put("Content-Type", "application/json");
        return handlers;
    }
}
