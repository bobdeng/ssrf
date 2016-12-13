# Simple Spring RestTemplate Framework
#Write your RestClient interface

	public interface ITestRest extends BaseRestClient {
    	@RestClient(method = BaseRestClient.METHOD_GET)
    	Weather getWeather(@PathParam(name = "id") String id, @Param(name = "test")String name, @Header("session")String session);
	}
#Spring bean
	@Bean
    	public ITestRest testRest(RestTemplate restTemplate){
        	ITestRest testRest= RestClientBuilder.newRestClient(ITestRest.class,restTemplate);
        	return  testRest;
   	 }
    	@Bean
    	public RestTemplate restTemplate(){
	
        	return new RestTemplateBuilder().build();
    	}
#use ResetClient
	@Autowired
	ITestRest testRest;
	@Test
	public void testGet() {
		testRest.setUrl("http://www.weather.com.cn/data/sk/{id}.html");
		Weather weather=testRest.getWeather("101010100","beijing","1111111");
		assertNotNull(weather);
		System.out.println(weather);
	}
