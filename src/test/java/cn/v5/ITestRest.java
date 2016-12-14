package cn.v5;

import cn.v5.annotations.*;
import cn.v5.bean.BaseRestClient;
import cn.v5.model.UserInfo;
import org.springframework.http.HttpMethod;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
public interface ITestRest extends BaseRestClient {
    @RestClient(method = HttpMethod.GET)
    UserInfo getUser(@PathParam(value = "id") String id);
    @RestClient(method = HttpMethod.POST)
    UserInfo postUser(@PathParam(value = "id") String id,@FormBody UserForm form);
    @RestClient(method = HttpMethod.POST,hasFile = true)
    UserInfo postUserWithFile(@PathParam(value = "id") String id,@FormBody UserFormWithFile form);
    @RestClient(method = HttpMethod.POST,hasFile = true)
    UserInfo postUserWithByteArray(@PathParam(value = "id") String id,@FormBody UserFormWithByteArray form);
}
