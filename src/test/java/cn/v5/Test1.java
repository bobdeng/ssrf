package cn.v5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@RunWith(JUnit4.class)
public class Test1 {
    @Test
    public void testUri(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://www.weather.com.cn/data/sk/{id}.html");
        System.out.println(builder.build().encode().toString());
    }
}
