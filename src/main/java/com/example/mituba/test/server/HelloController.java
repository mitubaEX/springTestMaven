package com.example.mituba.test.server;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.multipart.*;
import java.io.*;
import java.util.stream.*;
import java.util.*;

import com.mituba.searcher.*;

@Controller
public class HelloController {
    private int allTime = 0;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav){
        mav.setViewName("index");
        mav.addObject("msg", "お名前を書いて送信してください．");
        return mav;
	}

	// jar application/java-archive
	// class application/octet-stream
    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("upload")MultipartFile file, ModelAndView mav){
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
    		if(file.getOriginalFilename().contains(".jar"))
        		System.out.println("jar");
        	else if(file.getOriginalFilename().contains(".class"))
        		System.out.println("class");
        	new TextReader(br, "2gram", "8982", "69").createSearcherCollecter(br)
                .forEach(n -> onlySearch(n.collectSearcher()));
        	// mav.addObject("note", String.join("\n", readList));
        	// mav.addObject("value", String.join("\n", readList));
        	mav.setViewName("index");
        }catch(Exception e){

        }
        return mav;
    }

    public void onlySearch(Stream<SearchEngine> stream){
        long start = System.currentTimeMillis();
        stream.forEach(n -> simCheck(n.runOnlySearch()));
        long end = System.currentTimeMillis();
        allTime += (end - start);
        System.out.println(allTime + "ms");
    }

    public void simCheck(List<String[]> sim){
        sim.stream()
            .forEach(n -> System.out.println(n[0] + "," + n[1] + "," + n[2]));
    }

    public List<String> readFile(BufferedReader br){
    	return br.lines().collect(Collectors.toList());
    }
}
