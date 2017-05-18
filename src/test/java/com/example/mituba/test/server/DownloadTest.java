package com.example.mituba.test.server;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DownloadTest {
	@Test
	public void downloadCsvTestOfOneString(){
		List<String> list = Arrays.asList("test");
		downloadCsvTest(list);
	}
	
	@Test
	public void downloadCsvTestOfSeveralString(){
		List<String> list = Arrays.asList("test", "hello", "world");
		downloadCsvTest(list);
	}
	
	public void downloadCsvTest(List<String> list) {
		try {
			ResponseEntity<byte[]> response = new HelloController().download(String.join("\n", list));
			Path tempPath = Files.createTempFile("temp", ".csv");
			try(FileOutputStream fileOutputStream = new FileOutputStream(tempPath.toFile())){
				fileOutputStream.write(response.getBody());
			}
			try(BufferedReader br = new BufferedReader(new FileReader(tempPath.toFile()))){
				assertEquals(String.join("\n", br.lines().collect(Collectors.toList())), String.join("\n", list));
			}
//			assertEquals(response.getBody()[0], "test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

