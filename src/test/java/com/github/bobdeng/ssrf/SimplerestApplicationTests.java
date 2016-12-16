package com.github.bobdeng.ssrf;

import com.github.bobdeng.ssrf.forms.UserForm;
import com.github.bobdeng.ssrf.forms.UserFormWithByteArray;
import com.github.bobdeng.ssrf.forms.UserFormWithFile;
import com.github.bobdeng.ssrf.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Config.class})
public class SimplerestApplicationTests {

	@Autowired
	ITestRest testRest;
	@Test
	public void testGet() {
		UserInfo user=testRest.getUser("123456");
		assertNotNull(user);
	}

	@Test
	public void testInThread(){
		ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
		runTest(()->{
			List<Future> futureList=new ArrayList<>();
			for(int i=0;i<5000;i++){
				futureList.add(executorService.submit(()->testGet()));
				futureList.add(executorService.submit(()->testPost()));
			}
			for(Future future:futureList){
				try {
					future.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Test
	public void testPost() {
				UserInfo user = testRest.postUser("123456", UserForm.builder().tags(new String[]{"music", "movie"}).name("bobdeng_post").build());
				assertNotNull(user);
				assertEquals(user.getName(), "bobdeng_post");
				assertEquals(user.getTags(), new String[]{"music", "movie"});
	}
	@Test
	public void testPostUseJson() {
		UserInfo user = testRest.postUserUseJson("123456", UserForm.builder().tags(new String[]{"music", "movie"}).name("bobdeng_post").build());
		assertNotNull(user);
		assertEquals(user.getName(), "bobdeng_post");
		assertEquals(user.getTags(), new String[]{"music", "movie"});
	}
	@Test
	public void testPostWithFile() {
		testRest.setUrl("http://localhost:8080/postWithFile/{id}");
		UserInfo user=testRest.postUserWithFile("123456", UserFormWithFile.builder()
				.tags(new String[]{"music","movie"})
				.avatar(getResourceFile("img1.jpg"))
				.album(new File[]{getResourceFile("img2.jpg"),
						getResourceFile("img3.jpg")})
				.name("bobdeng1")
				.build());
		assertNotNull(user);
		assertEquals(user.getName(),"bobdeng1");
		assertArrayEquals(user.getTags(),new String[]{"music","movie"});
	}

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
			assertArrayEquals(user.getTags(), new String[]{"music", "movie"});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@Test
	public void testPut() {
		testRest.setUrl("http://localhost:8080/put/{id}");
		UserInfo user=testRest.putUser("123456", UserForm.builder().tags(new String[]{"music","movie"}).name("bobdeng").build());
		assertNotNull(user);
		assertEquals(user.getStatusCode(),200);
		assertEquals(user.getName(),"bobdeng");
		assertArrayEquals(user.getTags(),new String[]{"music","movie"});
	}

	private void runTest(Runnable runnable){
		long start=System.currentTimeMillis();
		runnable.run();
		System.out.println(System.currentTimeMillis()-start);
	}
	private File getResourceFile(String name){
		return new File(this.getClass().getClassLoader().getResource(name).getFile());
	}
	private byte[] readFile(File file)throws IOException{
		InputStream input=new FileInputStream(file);
		byte[] array=new byte[input.available()];
		input.read(array);
		input.close();
		return array;
	}

}
