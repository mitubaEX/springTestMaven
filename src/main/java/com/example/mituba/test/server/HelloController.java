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

	@RequestMapping(value="/compare", method=RequestMethod.GET)
	public ModelAndView compare(ModelAndView mav){
        List<String> tmpList = new ArrayList<>();
        tmpList.add("a,b,9 179 177 25 180,179 177 25 180 180,177 25 180 180 182,25 180 180 182 54,180 180 182 54 25,180 182 54 25 4,182 54 25 4 182,54 25 4 182 25,25 4 182 25 187,4 182 25 187 89,182 25 187 89 25,25 187 89 25 180,187 89 25 180 180,89 25 180 180 182,180 180 182 54 21,180 182 54 21 184,182 54 21 184 183,54 21 184 183 182,21 184 183 182 25,184 183 182 25 187,183 182 25 187 89,187 89 25 180 58,89 25 180 58 25,25 180 58 25 180,180 58 25 180 182,58 25 180 182 25,25 180 182 25 182,180 182 25 182 182,182 25 182 182 184,25 182 182 184 21,182 182 184 21 182,182 184 21 182 183,184 21 182 183 182,21 182 183 182 177,182 183 182 177 25,183 182 177 25 25,182 177 25 25 192,177 25 25 192 182,25 25 192 182 178,25 192 182 178 176,192 182 178 176 25,182 178 176 25 199,178 176 25 199 187,176 25 199 187 89,25 199 187 89 183,199 187 89 183 191,187 89 183 191 25,89 183 191 25 25,183 191 25 25 181,191 25 25 181 25,25 25 181 25 183,25 181 25 183 177");
        new Comparator().getCompareResult(tmpList, "2-gram");
        mav.setViewName("index");
        mav.addObject("compareResult", "world");
        return mav;
	}
    
	// jar application/java-archive
	// class application/octet-stream
    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("upload")MultipartFile file, @RequestParam("name")String rows, ModelAndView mav){
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
//            List<String[]> readList = new ArrayList<>();
    		if(file.getOriginalFilename().contains(".jar"))
        		System.out.println("jar");
        	else if(file.getOriginalFilename().contains(".class"))
        		System.out.println("class");

            new Extractor().createExtractFile(file);

            if(Objects.equals(rows, ""))//何も入力されなかったら，100件検索する．
                rows = "100";

//            readFile(new BufferedReader(new FileReader(new File("./test.txt"))), rows).stream()
//                .forEach(n -> n.searchPerform().forEach(m -> searchResult.add(m)));
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
