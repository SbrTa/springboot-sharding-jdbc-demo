package com.spartajet.shardingboot;

import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.spartajet.shardingboot.bean.Tick;
import com.spartajet.shardingboot.mapper.TickMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootShardingJdbcDemoApplicationTests {
    @Autowired
    private TickMapper tickMapper;
    @Autowired
    private CommonSelfIdGenerator commonSelfIdGenerator;


    @Test
    public void insertTest() {
        Tick tick = new Tick(commonSelfIdGenerator.generateId().longValue(), "a", "sh", 300, 100, getDate("2017-05-07"));
        this.tickMapper.insertTick(tick);
    }

    @Test
    public void selectAllTest() {
        List<Tick> list = this.tickMapper.listTickAll();
        list.forEach(System.out::println);
    }

    @Test
    public void selectByConditionTest() {
        List<Tick> list = this.tickMapper.listTickByCondition("a", "sh", getDate("2017-03-01"), getDate("2017-10-05"));
        list.forEach(System.out::println);
    }

    @Test
    public void selectForPageTest() {
        List<Tick> list = this.tickMapper.listTickForPage("a", "sh", getDate("2017-01-01"), getDate("2017-10-30"), 1, 10);
        list.forEach(System.out::println);
    }

    @Test
    public void testDate(){
        System.out.println(getDate("2017-03-01"));
    }
    private Date getDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Date.valueOf(LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE));
    }
}
