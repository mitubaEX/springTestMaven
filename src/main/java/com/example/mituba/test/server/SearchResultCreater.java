package com.example.mituba.test.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultCreater {
	public List<SearchResult> getSearchResultOfSearchResult(String searchResult){
		return Arrays.stream(searchResult.split("\n"))
				.map(i -> i.split(","))
				.filter(i -> i.length >= 3)
				.map(i -> new SearchResult(i[0], i[1], Double.parseDouble(i[2])))
				.collect(Collectors.toList());
	}
	
	public List<SearchResult> getSearchResultOfSearchResult(List<String> searchResult){
		return searchResult.stream() 
				.map(i -> i.split(","))
				.filter(i -> i.length >= 3)
				.map(i -> new SearchResult(i[0], i[1], Double.parseDouble(i[2])))
				.collect(Collectors.toList());
	}
	
    public List<Searcher> readFile(BufferedReader br, String rows){
    	return br.lines()
            .map(n -> n.split(",",3))
            .filter(n -> n.length >= 3)
            .map(n -> new Searcher(n[0], n[2],"8982", "2gram", rows))
            .collect(Collectors.toList());
    }
	
	public List<String> getSearchResultOfString(String fileName, String rows) throws FileNotFoundException{
		return readFile(new BufferedReader(new FileReader(new File(fileName + ".csv"))), rows).stream()
			.flatMap(n -> n.searchPerform().stream())
					.distinct()
					.collect(Collectors.toList());
	}
}
