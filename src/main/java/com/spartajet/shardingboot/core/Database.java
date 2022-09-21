package com.spartajet.shardingboot.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Database {
    private String name;
    private String url;
    private String username;
    private String password;
    private String driveClassName;
}
