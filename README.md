# Simple Spring RestTemplate Framework
#Maven
	<dependency>
			<groupId>com.github.bobdeng</groupId>
			<artifactId>ssrf</artifactId>
			<version>1.0.1</version>
	</dependency>
#Spring bean
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
#REST client interface
	public interface ITestRest extends BaseRestClient
##GET method
Method in ITestRest

	@RestClient(method = HttpMethod.GET)
    UserInfo getUser(@PathParam(value = "id") String id);
Invoke REST

	@Test
	public void testGet() {
		testRest.setUrl("http://localhost:8080/get/{id}");
		UserInfo user=testRest.getUser("123456");
		assertNotNull(user);
	}
	
##POST method
Form withoud file

	public class UserForm {
	    @Param("name")
	    private String name;
	    //withoud @Param, use field name as default paramter name
	    private String[] tags;
	}
Method in ITestRest

	@RestClient(method = HttpMethod.POST)
	UserInfo postUser(@PathParam(value = "id") String id,@FormBody UserForm form);
	
Invoke

	@Test
	public void testPost() {
		testRest.setUrl("http://localhost:8080/post/{id}");
		UserInfo user=testRest.postUser("123456", UserForm.builder().tags(new String[]{"music","movie"}).name("bobdeng").build());
		assertNotNull(user);
		assertEquals(user.getName(),"bobdeng");
		assertEquals(user.getTags(),new String[]{"music","movie"});
	}
##POST method with file
Form with files

	public class UserFormWithFile {
	    @Param("name")
	    private String name;
	    private String[] tags;
	    private File avatar;
	    private File[] album;
	}
Method in ITestRest

    @RestClient(method = HttpMethod.POST,hasFile = true)
    UserInfo postUserWithFile(@PathParam(value = "id") String id,@FormBody UserFormWithFile form);

Invoke

	@Test
	public void testPostWithFile() {
		testRest.setUrl("http://localhost:8080/postWithFile/{id}");
		UserInfo user=testRest.postUserWithFile("123456", UserFormWithFile.builder()
				.tags(new String[]{"music","movie"})
				.avatar(getResourceFile("img1.jpg"))
				.album(new File[]{getResourceFile("img2.jpg"),
						getResourceFile("img3.jpg")})
				.name("bobdeng")
				.build());
		assertNotNull(user);
		assertEquals(user.getName(),"bobdeng");
		assertEquals(user.getTags(),new String[]{"music","movie"});
	}
##POST method with byte[]
Form with byte[]

	public class UserFormWithByteArray {
	    @Param("name")
	    private String name;
	    private String[] tags;
	    private byte[] avatar;
	    private byte[][] album;
	}
Method in ITestRest

    @RestClient(method = HttpMethod.POST,hasFile = true)
    UserInfo postUserWithByteArray(@PathParam(value = "id") String id,@FormBody UserFormWithByteArray form);

Invoke
	
	@Test
	public void testPostWithByteArray() {
		try {
			testRest.setUrl("http://localhost:8080/postWithByteArray/{id}");
			UserInfo user = testRest.postUserWithByteArray("123456", UserFormWithByteArray.builder()
					.tags(new String[]{"music", "movie"})
					.avatar(readFile(getResourceFile("img1.jpg")))
					.album(new byte[][]{readFile(getResourceFile("img2.jpg")),readFile(getResourceFile("img3.jpg"))})
					.name("bobdeng")
					.build());
			assertNotNull(user);
			assertEquals(user.getName(), "bobdeng");
			assertEquals(user.getTags(), new String[]{"music", "movie"});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
##Add header
	UserInfo getUser(@Header(value = "session_id") String sessionId);
will add a header name is "session_id" in http header.