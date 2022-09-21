package com.spartajet.shardingboot.strategy;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;

@Slf4j
@Service
public class DatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {
    /**
     * Calculate shard result name set based on shard value and SQL's = operator.<br/>
     *  <p>do Equal Sharding uses = as a conditional sharding key in WHERE. Use sharding Value.get Value() in the algorithm to get the value after equal =</p>
     *
     * @param availableTargetNames A collection of all available target names, typically data source or table names
     * @param shardingValue        Sharding value
     *
     * @return The target name pointed to after sharding, usually the name of the data source or table
     */
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {

        log.info("[Database Policy] The current SQL statement is: = conditional statement, and the value of the shard build is: {}", new GsonBuilder().create().toJson(shardingValue));

        String databaseName = "";
        for (String targetName : availableTargetNames) {
            if (targetName.endsWith(shardingValue.getValue())) {
                databaseName = targetName;
                break;
            }
        }
        log.info("[Database Policy] The current SQL statement is: = conditional statement, and the target database is: {}",databaseName);

        return databaseName;
    }

    /**
     * Calculate shard result name set based on shard value and SQL's IN operator.<br/>
     *
     * <p>do In Sharding uses IN as the conditional sharding key in WHERE. Use sharding Value.get Values() in the algorithm to get the value after IN</p>
     *
     * @param availableTargetNames A collection of all available target names, typically data source or table names
     * @param shardingValue        Sharding value
     *
     * @return The set of target names pointed to after sharding, usually the data source or table name
     */
    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {

        log.info("[Database strategy] The current SQL statement is: in conditional statement, and the target database set is obtained through the value of the IN conditional sharding key; the value of sharding is: {}", new GsonBuilder().create().toJson(shardingValue));

        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (String value : shardingValue.getValues()) {
            for (String tableName : availableTargetNames) {
                if (tableName.equals(value)) {
                    result.add(tableName);
                }
            }
        }
        log.info("[Database Policy] The target database set of the current SQL statement is: {}",new GsonBuilder().create().toJson(result));
        return result;
    }

    /**
     * Calculate the set of shard result names based on the shard value and SQL's BETWEEN operator.<br/>
     * <p>do Between Sharding uses BETWEEN as the conditional sharding key in WHERE. Use sharding Value.get Value Range() in the algorithm to get the value after BETWEEN</p>
     *
     * @param availableTargetNames A collection of all available target names, typically data source or table names
     * @param shardingValue        Sharding value
     *
     * @return The set of target names pointed to after sharding, usually the data source or table name
     */
    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {

        log.error("[Database Policy] The shard key does not support querying using the between...and syntax");
        return null;

//        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
//        Range<String> range = (Range<String>) shardingValue.getValueRange();
//        for (String i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
//            for (String each : tableNames) {
//                if (each.endsWith(i % 2 + "")) {
//                    result.add(each);
//                }
//            }
//        }
    }
}
