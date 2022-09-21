package com.spartajet.shardingboot.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.spartajet.shardingboot.core.Database;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Configuration
public class DataSourceConfig {
    @Value("classpath:database.json")
    private Resource databaseFile;

    @Bean
    @Lazy
    public List<Database> databases() throws IOException {
        String databasesString = IOUtils.toString(databaseFile.getInputStream(), Charset.forName("UTF-8"));
        List<Database> databases = new Gson().fromJson(databasesString, new TypeToken<List<Database>>() {}.getType());
        return databases;
    }


//    @Bean
//    public HashMap<String, DataSource> dataSourceMap(List<Database> databases) {
//        HashMap<String, DataSource> dataSourceMap = new HashMap<>();
//        for (Database database : databases) {
//            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//            dataSourceBuilder.url(database.getUrl());
//            dataSourceBuilder.driverClassName(database.getDriveClassName());
//            dataSourceBuilder.username(database.getUsername());
//            dataSourceBuilder.password(database.getPassword());
//            DataSource dataSource = dataSourceBuilder.build();
//            dataSourceMap.put(database.getName(), dataSource);
//        }
//        return dataSourceMap;
//    }


    @Bean(name = "dataSource_bd")
    @Qualifier(value = "dataSource_bd")
    @ConfigurationProperties(prefix = "spring.datasource_bd")
    public DataSource datasource_bd() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }


    @Bean(name = "dataSource_us")
    @Qualifier(value = "dataSource_us")
    @ConfigurationProperties(prefix = "spring.datasource_us")
    public DataSource datasource_us() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }

    @Bean
    @Autowired
    public HashMap<String, DataSource> dataSourceMap(@Qualifier(value = "dataSource_bd") DataSource dataSource_bd ,@Qualifier(value = "dataSource_us")DataSource dataSource_us) {
        HashMap<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("db_bd",dataSource_bd);
        dataSourceMap.put("db_us",dataSource_us);
        return dataSourceMap;
    }


    @Bean
    @Primary
    public DataSource shardingDataSource(HashMap<String, DataSource> dataSourceMap, DatabaseShardingStrategy databaseShardingStrategy, TableShardingStrategy tableShardingStrategy) {
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
        TableRule tableRule = TableRule.builder("payment").actualTables(Arrays.asList(
                "db_bd.payment_bdt_2022_08", "db_bd.payment_bdt_2022_09", "db_bd.payment_usd_2022_08", "db_bd.payment_usd_2022_09",
                "db_us.payment_bdt_2022_08", "db_us.payment_bdt_2022_09", "db_us.payment_usd_2022_08", "db_us.payment_usd_2022_09")).dataSourceRule(dataSourceRule).build();
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule).tableRules(Arrays.asList(tableRule)).databaseShardingStrategy(databaseShardingStrategy).tableShardingStrategy(tableShardingStrategy).build();
        DataSource shardingDataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        return shardingDataSource;
    }
}
