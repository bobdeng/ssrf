package com.github.bobdeng.ssrf.bean;

import com.github.bobdeng.ssrf.annotations.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

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
            if(restClient!=null){
                return http(restClient,method,args);
            }
            return null;
        }
    }

    private Object http(RestClient client, Method method, Object[] args) {
        switch(client.method()){
            case DELETE:
                return doGet(client,method,args);
            case GET:
                return doGet(client,method,args);
            case POST:
                return doPost(client,method,args);
            case PATCH:
                return doPost(client,method,args);
            case PUT:
                return doPost(client,method,args);
            default:
                return doGet(client, method, args);
        }
    }

    private Object doGet(RestClient client, Method method, Object[] args) {
        HttpHeaders headers=getHeaders(method, args);
        HttpEntity httpEntity=new HttpEntity(headers);
        ResponseEntity responseEntity= restTemplate.exchange(rebuildUrl(client,method, args), client.method(),httpEntity,method.getReturnType(),args);
        Object result=  responseEntity.getBody();
        setResultHttpCode(result,responseEntity.getStatusCodeValue());
        return result;
    }
    private void setResultHttpCode(Object result,int code){
        try {
            Field field=result.getClass().getDeclaredField("statusCode");
            if(field!=null && field.getType()==int.class){
                field.setAccessible(true);
                field.set(result,code);
            }
        } catch (Exception e) {
        }
    }
    private Object doPost(RestClient client, Method method, Object[] args)  {
        HttpHeaders headers=getHeaders(method, args);
        //only post support multipart/form-data
        headers.setContentType((client.hasFile() && client.method()==HttpMethod.POST)?MediaType.MULTIPART_FORM_DATA:MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,Object> body=new LinkedMultiValueMap<>();
        for(int i=0;i<args.length;i++){
            FormBody formBody=findAnnotation(FormBody.class,method.getParameterAnnotations()[i]);
            if(formBody!=null){
                objectsToForm(args[i],body);
            }
        }
        HttpEntity<MultiValueMap<String,String>> httpEntity=new HttpEntity(body,headers);
        ResponseEntity responseEntity=  restTemplate.exchange(rebuildUrl(client,method, args),client.method(),httpEntity,method.getReturnType());
        Object result=  responseEntity.getBody();
        setResultHttpCode(result,responseEntity.getStatusCodeValue());
        return result;
    }

    private void objectsToForm(Object arg, MultiValueMap<String, Object> body){
        if(arg==null) return;
        try {
            Field[] fields = arg.getClass().getDeclaredFields();
            for (Field field : fields) {
                Param param = field.getAnnotation(Param.class);
                field.setAccessible(true);
                Object value = field.get(arg);
                String name = param != null ? param.value() : field.getName();
                if (field.getType().isArray() && field.getType() != byte[].class) {
                    for (int i = 0; i < Array.getLength(value); i++) {
                        Object arrayValue = Array.get(value, i);
                        object2Form(name, arrayValue, body);
                    }
                } else {
                    object2Form(name, value, body);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    private void object2Form(String name,Object arg, MultiValueMap<String, Object> body){
        if(arg==null) return;
        if(arg.getClass()==byte[].class){
            body.add(name,new ByteArrayResource((byte[])arg){
                @Override
                public String getFilename() {
                    return UUID.randomUUID().toString();
                }
            });
        }else if(arg.getClass()==File.class){
            body.add(name,new FileSystemResource((File)arg));
        }else{
            body.add(name,arg.toString());
        }
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

    private String rebuildUrl(RestClient client,Method method, Object[] args) {
        String url = StringUtils.isEmpty(client.path())?this.url.get():client.path();
        for(int i=0;i<args.length;i++){
            PathParam pathParam=findAnnotation(PathParam.class,method.getParameterAnnotations()[i]);
            if(pathParam!=null){
                url=url.replace("{"+pathParam.value()+"}",args[i]==null?"":args[i].toString());
            }
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for(int i=0;i<args.length;i++){
            Param param=findAnnotation(Param.class,method.getParameterAnnotations()[i]);
            if(param!=null){
                builder.queryParam(param.value(),args[i]==null?"":args[i].toString());
            }
        }
        return builder.build().encode().toString();
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
        return url.replace("{"+pathParam.value()+"}",value==null?"":value.toString());
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
