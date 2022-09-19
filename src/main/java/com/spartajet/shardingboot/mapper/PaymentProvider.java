package com.spartajet.shardingboot.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.util.StringUtils;

import java.sql.Date;


public class PaymentProvider {


    private final static String LIST_SQL ="select t.id , t.`name` , t.exchange , t.ask , t.bid , t.time from payment as t where 1=1 ";

    public String listPaymentByCondition(@Param(value = "name")String name , @Param(value = "exchange") String exchange , @Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate){
        StringBuilder sb = new StringBuilder(LIST_SQL);


        if(!StringUtils.isEmpty(name)){
            sb.append(" and t.name = #{name} ");
        }
        if(!StringUtils.isEmpty(exchange)){
            sb.append(" and t.exchange = #{exchange} ");
        }
        if(startDate != null && endDate != null){
            sb.append(" and t.time between #{startDate} and #{endDate} ");
        }

        return sb.toString();

    }


    public String listPaymentForPage(@Param(value = "name")String name , @Param(value = "exchange") String exchange , @Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate, @Param(value = "startPage") Integer startPage, @Param(value = "pageSize") Integer pageSize){

        String sql = listPaymentByCondition(name,exchange,startDate,endDate);

        StringBuilder sb = new StringBuilder(sql);
        sb.append(" order by id ");
        if(startPage!=null && pageSize !=null){
            sb.append(" limit #{startPage} , #{pageSize} ");
        }
        return sb.toString();
    }

}
