package com.spartajet.shardingboot.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.util.StringUtils;

import java.sql.Date;


public class PaymentProvider {

    private final static String LIST_SQL ="select t.id , t.`currency` , t.region , t.amount , t.product , t.time from payment as t where 1=1 ";

    public String listPaymentByCondition(@Param(value = "currency")String currency , @Param(value = "region") String region , @Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate){
        StringBuilder sb = new StringBuilder(LIST_SQL);


        if(!StringUtils.isEmpty(currency)){
            sb.append(" and t.currency = #{currency} ");
        }
        if(!StringUtils.isEmpty(region)){
            sb.append(" and t.region = #{region} ");
        }
        if(startDate != null && endDate != null){
            sb.append(" and t.time between #{startDate} and #{endDate} ");
        }

        return sb.toString();

    }


    public String listPaymentForPage(@Param(value = "currency")String currency , @Param(value = "region") String region , @Param(value = "startDate") Date startDate, @Param(value = "endDate")Date endDate, @Param(value = "startPage") Integer startPage, @Param(value = "pageSize") Integer pageSize){

        String sql = listPaymentByCondition(currency,region,startDate,endDate);

        StringBuilder sb = new StringBuilder(sql);
        sb.append(" order by id ");
        if(startPage!=null && pageSize !=null){
            sb.append(" limit #{startPage} , #{pageSize} ");
        }
        return sb.toString();
    }

}
