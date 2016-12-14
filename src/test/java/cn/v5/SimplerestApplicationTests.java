package cn.v5;

import cn.v5.forms.UserForm;
import cn.v5.forms.UserFormWithByteArray;
import cn.v5.forms.UserFormWithFile;
import cn.v5.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimplerestApplicationTests {

	@Autowired
	ITestRest testRest;
	@Test
	public void testGet() {
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
				.avatar(getResourceFile("img1.jpg"))
				.album(new File[]{getResourceFile("img2.jpg"),
						getResourceFile("img3.jpg")})
				.name("bobdeng")
				.build());
		assertNotNull(user);
		assertEquals(user.getName(),"bobdeng");
		assertEquals(user.getTags(),new String[]{"music","movie"});
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
			assertEquals(user.getTags(), new String[]{"music", "movie"});
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
