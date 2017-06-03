package com.example.mituba.test.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompareResultCreater {
	List<String> compareResult = new ArrayList<>();
	public CompareResultCreater(String searchResult, String kindOfBirthmark, String uploadFile){
		this.compareResult =
				new Comparator().getCompareResult(Arrays.asList(searchResult.split("\n")), kindOfBirthmark, uploadFile);
	}

    public String readFileOfCompareResult(String str){
    	try(BufferedReader br = new BufferedReader(new FileReader(new File(str)))){
			List<String> list =  br.lines().collect(Collectors.toList());
			return String.join(",", list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
    }

	public List<String> getCompareResultOfString(){
		return compareResult.stream()
			.distinct()
			.map(n -> readFileOfCompareResult(n))
			.collect(Collectors.toList());
	}

	public List<CompareResult> getCompareResultOfCompareResult(){
		return compareResult.stream()
			.distinct()
			.map(n -> readFileOfCompareResult(n))
			.map(n -> n.split(","))
			.map(n -> new CompareResult(n[1], Double.parseDouble(n[2])))
			.sorted((CompareResult1, CompareResult2) -> CompareResult2.riskNum.compareTo(CompareResult1.riskNum))
			.collect(Collectors.toList());
	}
}
