package com.spartajet.shardingboot.mapper;

import com.spartajet.shardingboot.bean.Payment;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("insert into payment (id,currency,region,amount,product,time) values (#{id},#{currency},#{region},#{amount},#{product},#{time})")
    void insertPayment(Payment payment);


    @SelectProvider(type = PaymentProvider.class , method = "listPaymentByCondition")
    List<Payment> listPaymentByCondition(@Param(value = "currency")String currency , @Param(value = "region") String region , @Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate);

    @Select("select t.id , t.`currency` , t.region , t.amount , t.product , t.time from payment as t")
    List<Payment> listAll();

    @SelectProvider(type = PaymentProvider.class , method = "listPaymentForPage")
    List<Payment> listPaymentForPage(@Param(value = "currency")String currency , @Param(value = "region") String region , @Param(value = "startDate") Date startDate, @Param(value = "endDate") Date endDate, @Param(value = "startPage") Integer startPage, @Param(value = "pageSize") Integer pageSize);

}
