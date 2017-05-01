package com.example.mituba.test.server;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.multipart.*;
import java.io.*;
import java.util.stream.*;
import java.util.*;
import java.net.URLEncoder;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.http.*;

import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

@Controller
public class HelloController {
    private int allTime = 0;
    List<String> searchResult = new ArrayList<>();

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav){
        mav.setViewName("index");
        return mav;
	}

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download() throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/csv; charset=UTF-8");
        h.setContentDispositionFormData("filename", "hoge.csv");
        return new ResponseEntity<>(String.join("\n", searchResult).getBytes("UTF-8"), h, HttpStatus.OK);
    }

	// jar application/java-archive
	// class application/octet-stream
    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("upload")MultipartFile file, @RequestParam("name")String rows, ModelAndView mav){
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            List<String[]> readList = new ArrayList<>();
    		if(file.getOriginalFilename().contains(".jar"))
        		System.out.println("jar");
        	else if(file.getOriginalFilename().contains(".class"))
        		System.out.println("class");

            new Extractor.createExtractFile(file);

            if(Objects.equals(rows, ""))//何も入力されなかったら，100件検索する．
                rows = "100";

            readFile(new BufferedReader(new FileReader(new File("./test.txt"))), rows).stream()
                .forEach(n -> n.searchPerform().forEach(m -> searchResult.add(m)));
        	mav.addObject("note", String.join("\n", searchResult));
        	mav.addObject("value", String.join("\n", searchResult));
        	mav.setViewName("index");
        }catch(Exception e){

        }
        return mav;
    }

    public List<Searcher> readFile(BufferedReader br, String rows){
    	return br.lines()
            .map(n -> n.split(",",3))
            .filter(n -> n.length >= 3)
            .map(n -> new Searcher(n[2],"8982", "2gram", rows, 0.75))
            .collect(Collectors.toList());
    }
}
