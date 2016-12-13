package cn.v5;

import cn.v5.annotations.Header;
import cn.v5.annotations.Param;
import cn.v5.annotations.PathParam;
import cn.v5.annotations.RestClient;
import cn.v5.bean.BaseRestClient;
import org.springframework.http.HttpMethod;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
public interface ITestRest extends BaseRestClient {
    @RestClient(method = HttpMethod.GET)
    Weather getWeather(@PathParam(value = "id") String id, @Param(value = "test")String name, @Header("session")String session);
}
