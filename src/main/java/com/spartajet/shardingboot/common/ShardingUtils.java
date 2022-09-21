package com.spartajet.shardingboot.common;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class ShardingUtils {
    /**
     * Get the default collection of shard keys
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
