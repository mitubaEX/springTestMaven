package com.example.mituba.test.server;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.multipart.*;
import java.io.*;
import java.util.stream.*;
import java.util.*;

import org.springframework.http.*;


@Controller
public class HelloController {
    List<String> searchResult = new ArrayList<>();
    List<String> compareResult = new ArrayList<>();
    String uploadFile = "";

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav){
        mav.setViewName("index");
        return mav;
	}

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@RequestParam("searchResult")String searchResultOfClient) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/csv; charset=UTF-8");
        h.setContentDispositionFormData("filename", "hoge.csv");
        return new ResponseEntity<>(searchResultOfClient.getBytes("UTF-8"), h, HttpStatus.OK);
    }

	@RequestMapping(value="/compare", method=RequestMethod.POST)
	public ModelAndView compare(@RequestParam("searchResult")String searchResultOfClient,@RequestParam("uploadFile")String uploadFileOfClient,  ModelAndView mav){
        new Comparator().getCompareResult(Arrays.asList(searchResultOfClient.split("\n")), "2-gram", uploadFileOfClient);
        mav.setViewName("index");
        mav.addObject("searchResult", searchResultOfClient);
        mav.addObject("note", searchResultOfClient);
        mav.addObject("uploadFile", uploadFileOfClient);
        mav.addObject("compareResult", String.join("\n", compareResult));
        mav.addObject("searchResult_js", searchResultOfClient);
        mav.addObject("note_js", searchResultOfClient);
        mav.addObject("uploadFile_js", uploadFileOfClient);
        mav.addObject("compareResult_js", String.join("\n", compareResult));
        return mav;
	}
	
    public List<Searcher> readFile(BufferedReader br, String rows){
    	return br.lines()
            .map(n -> n.split(",",3))
            .filter(n -> n.length >= 3)
            .map(n -> new Searcher(n[2],"8982", "2gram", rows, 0.25))
            .collect(Collectors.toList());
    }
    
	// jar application/java-archive
	// class application/octet-stream
    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("upload")MultipartFile file, @RequestParam("name")String rows, @RequestParam("searchResult")String searchResultOfClient,  ModelAndView mav){
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
//            List<String[]> readList = new ArrayList<>();
            List<String> searchResult = new ArrayList<>();
    		if(file.getOriginalFilename().contains(".jar"))
        		System.out.println("jar");
        	else if(file.getOriginalFilename().contains(".class"))
        		System.out.println("class");

            new Extractor().createExtractFile(file);
            uploadFile = file.getOriginalFilename();

            if(Objects.equals(rows, ""))//何も入力されなかったら，100件検索する．
                rows = "100";

            readFile(new BufferedReader(new FileReader(new File(file.getOriginalFilename() + ".csv"))), rows).stream()
                .forEach(n -> n.searchPerform().forEach(m -> searchResult.add(m)));
        	mav.setViewName("index");
        	mav.addObject("note", String.join("\n", searchResult));
        	mav.addObject("uploadFile", uploadFile);
        	mav.addObject("searchResult", String.join("\n", searchResult));
        	mav.addObject("note_js", String.join("\n", searchResult));
        	mav.addObject("uploadFile_js", uploadFile);
        	mav.addObject("searchResult_js", String.join("\n", searchResult));
            return mav;
//        	mav.addObject("value", String.join("\n", searchResult));
        }catch(Exception e){
            return mav;
        }
    }
}
