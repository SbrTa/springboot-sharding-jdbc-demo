package com.spartajet.shardingboot.common;

import com.google.gson.GsonBuilder;


public class GsonUtils {
    public static String objToJson(Object obj){
        if(null == obj) {
            return null;
        }
        return new GsonBuilder().create().toJson(obj);
    }
}
