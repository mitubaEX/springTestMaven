package com.example.mituba.test.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultCreater {
	private String birthmark;
	public SearchResultCreater(String birthmark){
        this.birthmark = birthmark;
	}
	public List<SearchResult> getSearchResultOfSearchResult(String searchResult){
		return Arrays.stream(searchResult.split("\n"))
				.map(i -> i.split(","))
				.filter(i -> i.length >= 7)
				.map(i -> new SearchResult(i[0], i[1], Double.parseDouble(i[2]), i[3], i[4], i[5], i[6]))
				.collect(Collectors.toList());
	}
	
	public List<SearchResult> getSearchResultOfSearchResult(List<String> searchResult){
		return searchResult.stream() 
				.map(i -> i.split(","))
				.filter(i -> i.length >= 7)
				.map(i -> new SearchResult(i[0], i[1], Double.parseDouble(i[2]), i[3], i[4], i[5], i[6]))
				.collect(Collectors.toList());
	}
	
    public List<Searcher> readFile(BufferedReader br, String rows, String threshold){
    	return br.lines()
            .map(n -> n.split(",",4))
            .filter(n -> n.length >= 4)
            .map(n -> new Searcher(n[0], n[3],"8982", birthmark, rows, threshold)) //ここを変更すると異なるバースマークの検索結果を取得可能
            .collect(Collectors.toList());
    }
	
	public List<String> getSearchResultOfString(String fileName, String rows, String threshold) throws FileNotFoundException{
		return readFile(new BufferedReader(new FileReader(new File(fileName + ".csv"))), rows, threshold).stream()
			.flatMap(n -> n.searchPerform().stream())
					.distinct()
					.collect(Collectors.toList());
	}

	public List<String> getClassInformationList(List<SearchResult> list){
    	return list.stream().map(n -> n.getClassInformation()).collect(Collectors.toList());
	}
}
