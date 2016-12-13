# Simple Spring RestTemplate Framework
#Write your RestClient interface
'
public interface ITestRest extends BaseRestClient {
    @RestClient(method = BaseRestClient.METHOD_GET)
    Weather getWeather(@PathParam(name = "id") String id, @Param(name = "test")String name, @Header("session")String session);
}
'

