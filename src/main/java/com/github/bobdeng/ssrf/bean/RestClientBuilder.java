package com.github.bobdeng.ssrf.bean;

import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
public class RestClientBuilder {
    public static <T>T newRestClient(Class<T> clz, RestTemplate restTemplate){
        T ret= (T)Proxy.newProxyInstance(RestClientBuilder.class.getClassLoader(),new Class[]{clz},new RestInvocationHandler(restTemplate));
        return  ret;
    }
}
