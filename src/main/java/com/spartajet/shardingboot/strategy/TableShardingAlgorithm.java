package com.spartajet.shardingboot.strategy;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.google.common.collect.Sets;
import com.spartajet.shardingboot.common.GsonUtils;
import com.spartajet.shardingboot.common.ShardingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class TableShardingAlgorithm implements MultipleKeysTableShardingAlgorithm {
    /**
     * Calculate shard result name set based on shard value.<br/>
     *  <h3>Multi-shard key partition table algorithm</h3>
     *  <p>
     *      1. Get the value collection for each shard key;<br/>
     *      2. Get the Cartesian product of a shard key-value set；<br/>
     *      3. Assemble the table name first through the Cartesian product to match the target table；<br/>
     *      4. Returns the set of matched target tables；<br/>
     *  </p>
     *  <h3>Precautions for the multi-shard key table-sharding algorithm</h3>
     *  <p>
     *      1. It needs to be the same as the single shard key sharding strategy, and the three cases of = , IN , between...and should be handled separately；<br/>
     *      2. Mainly some shard keys cannot use between...and conditions；<br/>
     *      3. In the multi-sharding key sharding algorithm, if one of the sharding keys is not included in the shardingValues, the target table cannot be hit
     *         (multiple sharding keys either all exist or none of them exist, and if some of them exist, it will arrive at The target table cannot be hit);
     *         therefore, it is necessary to deal with the existence of the shard key part in the SQL condition<br/>
     *  </p>
     *
     * @param availableTargetNames A collection of all available target names, typically data source or table names
     * @param shardingValues       Shard value collection
     *
     * @return The set of target names pointed to after sharding, usually the data source or table name
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue<?>> shardingValues) {
        log.info("[Table strategy] targetTableNames:{}, the value corresponding to the shard key: {}", GsonUtils.objToJson(availableTargetNames),GsonUtils.objToJson(shardingValues));

        Collection<String> result = new LinkedHashSet<>();

        Set<Object> nameValueSet = getShardingValue(shardingValues,"currency");
        Set<Object> timeValueSet = getShardingValue(shardingValues,"time");

        //Calculate the Cartesian product of two sets
        Set<List<Object>> valueResult = Sets.cartesianProduct(nameValueSet, timeValueSet);

        log.info("[Library strategy] The Cartesian product of the shard key value of the current SQL statement is: {}",GsonUtils.objToJson(valueResult));

        for (List<Object> value : valueResult) {
            //Assemble the values of multiple shard keys to determine their corresponding target data tables
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM");

            java.sql.Date date = (java.sql.Date) value.get(1);
            String timeString = formatter.format(date);
            String suffix = (String)value.get(0) + "_" + timeString;

            for (String tableName : availableTargetNames) {
                if (tableName.endsWith(suffix)) {
                    result.add(tableName);
                }
            }
        }
        log.info("[Table strategy] The target data table collection is：{}",GsonUtils.objToJson(result));
        if(result.isEmpty())
            log.info("[Table strategy] ------error, the shard key value in the current SQL misses the target database table-----");

        return result;
    }

    /**
     * Get the set of shard values corresponding to the specified shard key
     *
     * @param shardingValues
     * @param shardingKey
     * @return
     */
    private  Set<Object> getShardingValue(final Collection<ShardingValue<?>> shardingValues, final String shardingKey) {
        Set<Object> valueSet = new HashSet<>();
        ShardingValue<?> shardingValue = null;
        //Get the value of the current shard key
        for (ShardingValue<?> each : shardingValues) {
            if (each.getColumnName().equals(shardingKey)) {
                shardingValue =  each;
                break;
            }
        }
        if (null == shardingValue) {
            return  ShardingUtils.defaultShardingValues(shardingKey);
        }
        switch (shardingValue.getType()) {
            case SINGLE:
                log.info("[Table strategy] The shard key condition corresponding to the current SQL statement is: equal(=)");
                valueSet.add(shardingValue.getValue());
                break;
            case LIST:
                log.info("[Table strategy] The shard key condition corresponding to the current SQL statement is: IN");
                valueSet.addAll(shardingValue.getValues());
                break;
            case RANGE:
                log.info("[Table strategy] The shard key condition corresponding to the current SQL statement is: between ... and");
                if("time".equals(shardingKey)){

                    Date start = new Date(((java.sql.Date)shardingValue.getValueRange().lowerEndpoint()).getTime());
                    Date end = new Date(((java.sql.Date)shardingValue.getValueRange().upperEndpoint()).getTime());

                    LocalDate startTime = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()).toLocalDate();
                    LocalDate endTime = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault()).toLocalDate();

                    LocalDate tableStart =  LocalDate.of(2022,8,1);
                    LocalDate tableend =  LocalDate.of(2022,9,1);

                    if(!tableStart.isBefore(startTime) && !tableStart.isAfter(endTime)){
                        valueSet.add(java.sql.Date.valueOf(tableStart));
                    }

                    if(!tableend.isBefore(startTime) && !tableend.isAfter(endTime)){
                        valueSet.add(java.sql.Date.valueOf(tableend));
                    }

                }else{
                    log.error("[Table policy] sorry, this key-value shard does not support the use of between ... and statements");
                }


                break;
            default:
                throw new UnsupportedOperationException();

        }
        log.info("[Table strategy] Shard key --{}-- The corresponding value are: {}",shardingKey,valueSet);

        return valueSet;
    }

    private java.sql.Date toDate(LocalDate date){
        return java.sql.Date.valueOf(date);
    }
}
