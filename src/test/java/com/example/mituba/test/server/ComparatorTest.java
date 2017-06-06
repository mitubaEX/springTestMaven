package com.example.mituba.test.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComparatorTest {
	FileControler fileControler = new FileControler();
	// 比較ファイルのファイル名がただし以下確かめる
	@Test
	public void compareFileNameTest(){
		String tmpString = new Comparator().compare("src/test/resources/FizzBuzz.class.csv", 
				"2-gram", 
				"src/test/resources/Test.class.csv"
				);
		assertEquals(tmpString, "src.test.resources.Test.class.csv-src.test.resources.FizzBuzz.class.csv-2-gram.csv");
		fileControler.deleteFile(tmpString);
	}
	
	// 比較ファイルの中身が正しいか確かめる
	@Test
	public void compareResultTest(){
		String tmpString = new Comparator().compare("src/test/resources/FizzBuzz.class", 
				"2-gram", 
				"src/test/resources/Test.class"
				);
		FileControler fileControler = new FileControler();
		assertEquals(fileControler.readFile(tmpString), fileControler.readFile("src/test/resources/src.test.resources.Test.class.csv-src.test.resources.FizzBuzz.class.csv-2-gram.csv"));
		fileControler.deleteFile(tmpString);
	}
	
	public void CompareExceptionTest(){
		assertEquals(new Comparator().compare(null, "2-gram", null), "");
	}
	
	@Test
	public void createFileNameTest(){
		String tmpString = new Comparator().createFile("a", "b", "2-gram");
		assertEquals(tmpString, "a.csv");
		assertEquals(fileControler.readFile(tmpString), "a,,2-gram,b");
		fileControler.deleteFile(tmpString);;
	}
	
	@Test
	public void createFileNameTestExist(){
		String tmpString = new Comparator().createFile("src/test/resources/FizzBuzz.class", "b", "2-gram");
		assertEquals(tmpString, "src/test/resources/FizzBuzz.class.csv");
	}
	
	@Test
	public void getCompareResultTest(){
		assertTrue(new Comparator().getCompareResult(new ArrayList<>(), "2-gram", "a").isEmpty());
		String[] tmpArray = {"a,b,c"};
		assertTrue(new Comparator().getCompareResult(Arrays.asList(tmpArray), "2-gram", "a").isEmpty());
	}
}
