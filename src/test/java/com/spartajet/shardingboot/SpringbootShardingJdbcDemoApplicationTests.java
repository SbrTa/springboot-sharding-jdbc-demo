package com.spartajet.shardingboot;

import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.spartajet.shardingboot.bean.Payment;
import com.spartajet.shardingboot.mapper.PaymentRepository;
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
    private PaymentRepository paymentRepository;
    @Autowired
    private CommonSelfIdGenerator commonSelfIdGenerator;


    @Test
    public void insertTest() {
        Payment payment = new Payment(commonSelfIdGenerator.generateId().longValue(), "usd", "sh", 300, 100, getDate("2022-09-11"));
        this.paymentRepository.insertPayment(payment);
    }

    @Test
    public void selectAllTest() {
        List<Payment> list = this.paymentRepository.listAll();
        list.forEach(System.out::println);
    }

    @Test
    public void selectByConditionTest() {
        List<Payment> list = this.paymentRepository.listPaymentByCondition("usd", "sz", getDate("2022-08-01"), getDate("2022-10-01"));
        list.forEach(System.out::println);
    }

    @Test
    public void selectForPageTest() {
        List<Payment> list = this.paymentRepository.listPaymentForPage("bdt", "sh", getDate("2022-01-01"), getDate("2022-10-30"), 1, 10);
        list.forEach(System.out::println);
    }

    @Test
    public void testDate(){
        System.out.println(getDate("2022-08-15"));
    }
    private Date getDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Date.valueOf(LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE));
    }
}
