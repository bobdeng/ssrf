package com.github.bobdeng.ssrf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by zhiguodeng on 2016/12/14.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private int statusCode;
    private String name;
    private int age;
    private String[] tags;
    private List<String> contacts;
}
