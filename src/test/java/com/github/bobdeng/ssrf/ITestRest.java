package com.github.bobdeng.ssrf;

import com.github.bobdeng.ssrf.annotations.FormBody;
import com.github.bobdeng.ssrf.bean.BaseRestClient;
import com.github.bobdeng.ssrf.forms.UserForm;
import com.github.bobdeng.ssrf.forms.UserFormWithByteArray;
import com.github.bobdeng.ssrf.forms.UserFormWithFile;
import com.github.bobdeng.ssrf.model.UserInfo;
import com.github.bobdeng.ssrf.annotations.PathParam;
import com.github.bobdeng.ssrf.annotations.RestClient;
import org.springframework.http.HttpMethod;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
public interface ITestRest extends BaseRestClient {
    @RestClient(method = HttpMethod.GET,path = "http://localhost:8080/get/{id}")
    UserInfo getUser(@PathParam(value = "id") String id);
    @RestClient(method = HttpMethod.POST,path = "http://localhost:8080/post/{id}")
    UserInfo postUser(@PathParam(value = "id") String id,@FormBody UserForm form);
    @RestClient(method = HttpMethod.POST,hasFile = true)
    UserInfo postUserWithFile(@PathParam(value = "id") String id,@FormBody UserFormWithFile form);
    @RestClient(method = HttpMethod.POST,hasFile = true)
    UserInfo postUserWithByteArray(@PathParam(value = "id") String id,@FormBody UserFormWithByteArray form);
    @RestClient(method = HttpMethod.PUT,hasFile = false)
    UserInfo putUser(@PathParam(value = "id") String id,@FormBody UserForm form);

    /**
     * Post put patch，body use json.
     * @param id
     * @param form
     * @return
     */
    @RestClient(method = HttpMethod.POST,path = "http://localhost.charlesproxy.com:8080/postUseJson/{id}",jsonBody = true)
    UserInfo postUserUseJson(@PathParam(value = "id") String id,@FormBody UserForm form);
}
