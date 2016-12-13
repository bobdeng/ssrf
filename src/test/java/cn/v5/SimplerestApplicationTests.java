package cn.v5;

import cn.v5.bean.RestClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimplerestApplicationTests {

	@Autowired
	ITestRest testRest;
	@Test
	public void testGet() {
		testRest.setUrl("http://www.weather.com.cn/data/sk/{id}.html");
		Weather weather=testRest.getWeather("101010100","beijing","1111111");
		assertNotNull(weather);
		System.out.println(weather);
	}


}
