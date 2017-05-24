package com.example.mituba.test.server;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExtractorTest {
	@Test
	public void extractorFileNameTest(){
		try {
			assertEquals(new Extractor().createOriginalFile(new MockMultipartFile("test", "test","text/csv",  new byte[0])), "test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void extractorFileNameTestForLargestName(){
        List<String> list = IntStream.rangeClosed(0, 100).mapToObj(n -> "test").collect(Collectors.toList());
		try {
			assertEquals(new Extractor().createOriginalFile(new MockMultipartFile("test", String.join("", list), "text/csv", new byte[0])), String.join("", list));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    assertThat(e.getMessage(), equalTo(String.join("", list) + ": File name too long"));
		}
	}
}

