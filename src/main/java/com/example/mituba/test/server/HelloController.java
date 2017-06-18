package com.example.mituba.test.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.multipart.*;
import java.io.*;
import java.util.*;
import org.springframework.http.*;

@Controller
public class HelloController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav){
        mav.setViewName("search");
        return mav;
	}

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@RequestParam("searchResult")String searchResultOfClient) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/csv; charset=UTF-8");
        h.setContentDispositionFormData("filename", "searchResult.csv");
        return new ResponseEntity<>(searchResultOfClient.getBytes("UTF-8"), h, HttpStatus.OK);
    }

    @RequestMapping(value = "/downloadCompareResult", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadCompareResult(@RequestParam("compareResult")String compareResultOfClient) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/csv; charset=UTF-8");
        h.setContentDispositionFormData("filename", "compareResult.csv");
        return new ResponseEntity<>(compareResultOfClient.getBytes("UTF-8"), h, HttpStatus.OK);
    }
    
	@RequestMapping(value="/compare", method=RequestMethod.GET)
	public ModelAndView compare(ModelAndView mav){
        mav.setViewName("search");
		return mav;
	}
	

	@RequestMapping(value="/compare", method=RequestMethod.POST)
	public ModelAndView compare(@RequestParam("searchResult")String searchResultOfClient,@RequestParam("uploadFile")String uploadFileOfClient, @RequestParam("birthmark")String birthmark,  ModelAndView mav){
		try{
			CompareResultCreater creater = new CompareResultCreater(searchResultOfClient, birthmark, uploadFileOfClient + ".csv");
			List<CompareResult> compareResultList  = creater.getCompareResultOfCompareResult();
			List<String> compareResultStringList  = creater.getCompareResultOfString();
//			List<SearchResult> searchResultList = new SearchResultCreater().getSearchResultOfSearchResult(searchResultOfClient);
//			searchResultList.stream().forEach(n -> new FileControler().deleteFile(n.filename + ".csv"));
			compareResultList.stream().forEach(n -> new FileControler().deleteFile(n.myFileName + ".class.csv-" +n.filename + ".csv-" + birthmark + ".csv"));
			
			mav.setViewName("compareResult");
			mav.addObject("searchResult", searchResultOfClient);
			mav.addObject("note", searchResultOfClient);
			mav.addObject("uploadFile", uploadFileOfClient);
			mav.addObject("compareResult", String.join("\n", compareResultStringList));
//			mav.addObject("searchResultList", searchResultList);
//			mav.addObject("compareResultList", compareResultList);
			mav.addObject("compareResultList", compareResultList);
			mav.addObject("searchResult_js", searchResultOfClient);
			mav.addObject("note_js", searchResultOfClient);
			mav.addObject("uploadFile_js", uploadFileOfClient);
			mav.addObject("compareResult_js", String.join("\n", compareResultStringList));
			return mav;
		}catch(Exception e){
			e.printStackTrace();
			mav.setViewName("searchResult");
        	mav.addObject("errorMessageOfCompare", "検索結果がないので比較できません");
			return mav;
		}
	}

    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("upload")MultipartFile file, @RequestParam("searchResult")String searchResultOfClient, @RequestParam("birthmark") String birthmark, ModelAndView mav){
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){

            new Extractor().extractBirthmark(file);
            String uploadFile = file.getOriginalFilename();

            String rows = "10";//デフォルトを10にしておく
			System.out.println(birthmark);

            List<String> searchResult = new SearchResultCreater(birthmark).getSearchResultOfString(file.getOriginalFilename(), rows);
            List<SearchResult> searchResultList = new SearchResultCreater(birthmark).getSearchResultOfSearchResult(searchResult);
//            List<String> classInformationList = new SearchResultCreater().getClassInformationList(searchResultList);
//            classInformationList.stream()
//					.forEach(System.out::println);
			List<String> compareResult = new ArrayList<>();
            
        	mav.setViewName("searchResult");
        	mav.addObject("note", String.join("\n", searchResult));
        	mav.addObject("uploadFile", uploadFile);
        	mav.addObject("searchResult", String.join("\n", searchResult));
        	mav.addObject("searchResultList", searchResultList);
        	mav.addObject("note_js", String.join("\n", searchResult));
        	mav.addObject("uploadFile_js", uploadFile);
        	mav.addObject("birthmark", birthmark);
        	mav.addObject("searchResult_js", String.join("\n", searchResult));
			mav.addObject("compareResult", String.join("\n", compareResult));
			mav.addObject("compareResult_js", String.join("\n", compareResult));
            return mav;
//        	mav.addObject("value", String.join("\n", searchResult));
        }catch(Exception e){
        	mav.setViewName("search");
        	mav.addObject("errorMessageOfSearch", "対応可能な形式はclassとjarです");
            return mav;
        }
    }
}
