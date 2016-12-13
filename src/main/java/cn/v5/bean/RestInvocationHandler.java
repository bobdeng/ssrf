package cn.v5.bean;

import cn.v5.annotations.Header;
import cn.v5.annotations.Param;
import cn.v5.annotations.PathParam;
import cn.v5.annotations.RestClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
public class RestInvocationHandler implements InvocationHandler {
    private ThreadLocal<String> url=new ThreadLocal<>();
    private RestTemplate restTemplate;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("setUrl") && args.length==1 && args[0].getClass()==String.class){
            this.url.set((String)args[0]);
            return null;
        }else {
            RestClient restClient=method.getAnnotation(RestClient.class);
            if(restClient==null || restClient.method().equals(BaseRestClient.METHOD_GET)){
                return doGet(method,args);
            }
            return null;
        }
    }

    private Object doGet(Method method, Object[] args) {
        UriComponentsBuilder builder = rebuildUrl(method, args);
        HttpHeaders headers=getHeaders(method, args);
        HttpEntity httpEntity=new HttpEntity(headers);
        ResponseEntity responseEntity= restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET,httpEntity,method.getReturnType(),args);
        return  responseEntity.getBody();
    }

    private HttpHeaders getHeaders(Method method, Object[] args) {
        HttpHeaders requestHeaders = new HttpHeaders();
        for(int i=0;i<args.length;i++){
            Header header=findAnnotation(Header.class,method.getParameterAnnotations()[i]);
            if(header!=null){
                requestHeaders.add(header.value(),args[i]==null?"":args[i].toString());
            }
        }
        return requestHeaders;
    }

    private UriComponentsBuilder rebuildUrl(Method method, Object[] args) {
        String url =this.url.get();
        for(int i=0;i<args.length;i++){
            PathParam pathParam=findAnnotation(PathParam.class,method.getParameterAnnotations()[i]);
            if(pathParam!=null){
                url=url.replace("{"+pathParam.name()+"}",args[i]==null?"":args[i].toString());
            }
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for(int i=0;i<args.length;i++){
            Param param=findAnnotation(Param.class,method.getParameterAnnotations()[i]);
            if(param!=null){
                builder.queryParam(param.name(),args[i]==null?"":args[i].toString());
            }
        }
        return builder;
    }

    private Object[] getVaribles(Object[] args) {
        return new Object[0];
    }

    private String getUrlString(Method method, Object[] args) {
        String url=this.url.get();
        for(int i=0;i<args.length;i++){
            Annotation[] annotations=method.getParameterAnnotations()[i];
            PathParam pathParam=findAnnotation(PathParam.class,annotations);
            if(pathParam!=null){
                url=replaceUrlHolder(url,pathParam,args[i]);
            }
        }
        return url;
    }

    private String replaceUrlHolder(String url, PathParam pathParam,Object value) {
        return url.replace("{"+pathParam.name()+"}",value==null?"":value.toString());
    }

    private <T>T findAnnotation(Class<T> clz,Annotation[] annotations) {
        for(Annotation annotation:annotations){
            if(annotation.annotationType()==clz){
                return (T)annotation;
            }
        }
        return null;
    }

    public RestInvocationHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
