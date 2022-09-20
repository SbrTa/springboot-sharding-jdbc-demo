package com.spartajet.shardingboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private long id;
    private String currency;
    private String region;
    private int ask;
    private int bid;
    private Date time;
}
