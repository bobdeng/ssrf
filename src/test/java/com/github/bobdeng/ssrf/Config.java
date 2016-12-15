package com.github.bobdeng.ssrf;

import com.github.bobdeng.ssrf.bean.RestClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
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
