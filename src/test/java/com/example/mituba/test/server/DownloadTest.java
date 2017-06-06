package com.example.mituba.test.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
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
		assertEquals(downloadCsvString(list), String.join("\n", list));
	}
	
	@Test
	public void downloadCsvTestOfSeveralString(){
		List<String> list = Arrays.asList("test", "hello", "world");
		assertEquals(downloadCsvString(list), String.join("\n", list));
	}
	
	@Test
	public void downloadCsvTestOfNull(){
		List<String> list = null;
		assertNull(downloadCsvString(list));
	}
	
	@Test
	public void downloadCsvTestOfOneStringCompareResult(){
		List<String> list = Arrays.asList("test");
		assertEquals(downloadCsvStringCompareResult(list), String.join("\n", list));
	}
	
	public String downloadCsvString(List<String> list) {
		try {
			ResponseEntity<byte[]> response = new HelloController().download(String.join("\n", list));
			Path tempPath = Files.createTempFile("temp", ".csv");
			try(FileOutputStream fileOutputStream = new FileOutputStream(tempPath.toFile())){
				fileOutputStream.write(response.getBody());
			}
			try(BufferedReader br = new BufferedReader(new FileReader(tempPath.toFile()))){
				return String.join("\n", br.lines().collect(Collectors.toList()));
			}
//			assertEquals(response.getBody()[0], "test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public String downloadCsvStringCompareResult(List<String> list) {
		try {
			ResponseEntity<byte[]> response = new HelloController().downloadCompareResult(String.join("\n", list));
			Path tempPath = Files.createTempFile("temp", ".csv");
			try(FileOutputStream fileOutputStream = new FileOutputStream(tempPath.toFile())){
				fileOutputStream.write(response.getBody());
			}
			try(BufferedReader br = new BufferedReader(new FileReader(tempPath.toFile()))){
				return String.join("\n", br.lines().collect(Collectors.toList()));
			}
//			assertEquals(response.getBody()[0], "test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}

