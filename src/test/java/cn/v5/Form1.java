package cn.v5;

import cn.v5.annotations.Param;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Form1 {
    @Param("name")
    private String name;
    @Param("age")
    private int age;
    @Param("tag")
    private List<String> tags;
    @Param("contact")
    private String[] contacts;
}
