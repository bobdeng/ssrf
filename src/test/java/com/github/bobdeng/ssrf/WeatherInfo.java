package com.github.bobdeng.ssrf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfo {
    private String city;
    private String cityid;
    private String temp;
    private String WD;
    private String WS;
    private String SD;
    private String WSE;
    private String time;
    private String rain;
}
