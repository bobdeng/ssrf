package cn.v5;

import cn.v5.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimplerestApplicationTests {

	@Autowired
	ITestRest testRest;
	@Test
	public void testGet() {
		testRest.setUrl("http://localhost:8080/get/{id}");
		UserInfo user=testRest.getUser("123456");
		assertNotNull(user);
		System.out.println(user);
	}
	@Test
	public void testPost() {
		testRest.setUrl("http://localhost:8080/post/{id}");
		UserInfo user=testRest.postUser("123456", UserForm.builder().tags(new String[]{"music","movie"}).name("bobdeng").build());
		assertNotNull(user);
		assertEquals(user.getName(),"bobdeng");
		assertEquals(user.getTags(),new String[]{"music","movie"});
	}
	@Test
	public void testPostWithFile() {
		testRest.setUrl("http://localhost:8080/postWithFile/{id}");
		UserInfo user=testRest.postUserWithFile("123456", UserFormWithFile.builder()
				.tags(new String[]{"music","movie"})
				.avatar(new File("/Users/zhiguodeng/Downloads/tuoguang.jpg"))
				.album(new File[]{new File("/Users/zhiguodeng/Downloads/IMG_20161113_193505.jpg"),
						new File("/Users/zhiguodeng/Downloads/IMG_20161113_193526.jpg")})
				.name("bobdeng")
				.build());
		assertNotNull(user);
		assertEquals(user.getName(),"bobdeng");
		assertEquals(user.getTags(),new String[]{"music","movie"});
	}

}
