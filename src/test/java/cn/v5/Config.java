package cn.v5;

import cn.v5.bean.RestClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@Configuration
public class Config
{
    @Bean
    public ITestRest testRest(RestTemplate restTemplate){
        ITestRest testRest= RestClientBuilder.newRestClient(ITestRest.class,restTemplate);
        return  testRest;
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder()
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter())
                .additionalMessageConverters(new FormHttpMessageConverter()).build();
    }
}
