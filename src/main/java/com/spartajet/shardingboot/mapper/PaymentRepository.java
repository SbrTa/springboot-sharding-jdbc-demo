package com.spartajet.shardingboot.mapper;

import com.spartajet.shardingboot.bean.Payment;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

/**
 * @description
 * @create 2017-02-07 下午9:58
 * @email gxz04220427@163.com
 */
@Mapper
public interface PaymentRepository {

    @Insert("insert into payment (id,name,exchange,ask,bid,time) values (#{id},#{name},#{exchange},#{ask},#{bid},#{time})")
    void insertPayment(Payment payment);


    @SelectProvider(type = PaymentProvider.class , method = "listPaymentByCondition")
    List<Payment> listPaymentByCondition(@Param(value = "name")String name , @Param(value = "exchange") String exchange , @Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate);

    @Select("select t.id , t.`name` , t.exchange , t.ask , t.bid , t.time from payment as t")
    List<Payment> listAll();

    @SelectProvider(type = PaymentProvider.class , method = "listPaymentForPage")
    List<Payment> listPaymentForPage(@Param(value = "name")String name , @Param(value = "exchange") String exchange , @Param(value = "startDate") Date startDate, @Param(value = "endDate") Date endDate, @Param(value = "startPage") Integer startPage, @Param(value = "pageSize") Integer pageSize);




}
