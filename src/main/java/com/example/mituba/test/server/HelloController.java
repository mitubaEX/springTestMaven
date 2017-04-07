package com.example.mituba.test.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.multipart.*;
import java.io.*;
import java.util.stream.*;
import java.util.*;

@Controller
public class HelloController {
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
        	List<String> readList = readFile(br);
        	mav.addObject("note", String.join("\n", readList));
        	mav.addObject("value", String.join("\n", readList));
        	mav.setViewName("index");
        }catch(Exception e){
        	
        }
        return mav;
    }
    
    public List<String> readFile(BufferedReader br){
    	return br.lines().collect(Collectors.toList());
    }
}
