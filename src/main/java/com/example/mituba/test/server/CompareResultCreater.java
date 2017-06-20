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
	List<ComparatorClassInformation> compareResult = new ArrayList<>();
	public CompareResultCreater(String searchResult, String kindOfBirthmark, String uploadFile){
		this.compareResult =
				new Comparator().getCompareResult(Arrays.asList(searchResult.split("\n")), kindOfBirthmark, uploadFile);
	}

    public String readFileOfCompareResult(String str, String jar,String groupID, String artifactID, String version){
    	try(BufferedReader br = new BufferedReader(new FileReader(new File(str)))){
			List<String> list =  br.lines().collect(Collectors.toList());
			return String.join(",", list) + "," + jar + "," + groupID + "," + artifactID + "," + version;
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
			.map(n -> readFileOfCompareResult(n.getFilePath(), n.getJar(), n.getGroupID(), n.getArtifactID(), n.getVersion()))
			.collect(Collectors.toList());
	}

	public List<CompareResult> getCompareResultOfCompareResult(){
		return compareResult.stream()
			.distinct()
			.map(n -> readFileOfCompareResult(n.getFilePath(), n.getJar(),n.getGroupID(), n.getArtifactID(), n.getVersion()))
			.map(n -> n.split(","))
			.map(n -> new CompareResult(n[0], n[1], Double.parseDouble(n[2]), n[3], n[4], n[5], n[6]))
			.sorted((CompareResult1, CompareResult2) -> CompareResult2.riskNum.compareTo(CompareResult1.riskNum))
			.collect(Collectors.toList());
	}
}
