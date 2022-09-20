package com.spartajet.shardingboot.common;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author hzj
 * @Date 2017/7/7 11:17
 * @Description :
 */
public class ShardingUtils {
    /**
     * 获取分片键的默认值集合
     *
     * @param shardingKey
     * @return
     */
    public static Set<Object> defaultShardingValues(String shardingKey){

        Set valueSet = new HashSet<Object>();

        if("currency".equals(shardingKey)){
            valueSet.add("bdt");
            valueSet.add("usd");
        }else if("time".equals(shardingKey)){
            valueSet.add(LocalDate.of(2022,8,1));
            valueSet.add(LocalDate.of(2022,9,1));
        }else if("region".equals(shardingKey)){
            valueSet.add("bd");
            valueSet.add("us");
        }
        return valueSet;
    }


}
