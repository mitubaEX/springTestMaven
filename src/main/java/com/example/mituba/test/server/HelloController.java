package com.example.mituba.test.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.multipart.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.*;

@Controller
public class HelloController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) throws IOException{
		List<String> list = new FileControler().readFileList("./documentNumber.csv");
        mav.setViewName("search");
        mav.addObject("documentNumber", list.get(list.size() - 1));
        return mav;
	}

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public ResponseEntity<byte[]> download(@RequestParam("searchResult")String searchResultOfClient) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/csv; charset=UTF-8");
        h.setContentDispositionFormData("filename", "searchResult.csv");

        searchResultOfClient = String.join("\n",Arrays.stream(searchResultOfClient.split("\n"))
				.map(n -> n.split(","))
				.map(n -> n[0] + "," + n[1] + "," + n[3] + "," + n[4] + "," + n[5] + "," + n[6] + "," + n[2])
				.collect(Collectors.toList()));
        return new ResponseEntity<>(searchResultOfClient.getBytes("UTF-8"), h, HttpStatus.OK);
    }

    @RequestMapping(value = "/downloadCompareResult", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadCompareResult(@RequestParam("compareResult")String compareResultOfClient) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/csv; charset=UTF-8");
        h.setContentDispositionFormData("filename", "compareResult.csv");
		compareResultOfClient = String.join("\n",Arrays.stream(compareResultOfClient.split("\n"))
				.map(n -> n.split(","))
				.map(n -> n[0] + "," + n[1] + "," + n[3] + "," + n[4] + "," + n[5] + "," + n[6].replace("\n","") + "," + n[2].replace("\n",""))
				.collect(Collectors.toList()));
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
		    String[] args = birthmark.split(",");
		    List<CompareResult> compareResultList = new ArrayList<>();
		    List<String> compareResultStringList = new ArrayList<>();
		    if(args.length >= 2) {
                CompareResultCreater creater = new CompareResultCreater(searchResultOfClient, args[0], uploadFileOfClient + "-" + args[0] + ".csv");
                compareResultList = creater.getCompareResultOfCompareResult();
                compareResultStringList = creater.getCompareResultOfString();
//			List<SearchResult> searchResultList = new SearchResultCreater().getSearchResultOfSearchResult(searchResultOfClient);
//			searchResultList.stream().forEach(n -> new FileControler().deleteFile(n.filename + ".csv"));
                compareResultList.stream().forEach(n -> new FileControler().deleteFile(n.myFileName + ".class-" + args[0] +".csv-" + n.filename + ".csv-" + args[0] + ".csv"));


                CompareResultCreater creater2 = new CompareResultCreater(searchResultOfClient, args[1], uploadFileOfClient + "-" + args[1] + ".csv");
                List<CompareResult> compareResultListTmp = creater2.getCompareResultOfCompareResult();
                List<String> compareResultStringListTmp = creater2.getCompareResultOfString();
//			List<SearchResult> searchResultList = new SearchResultCreater().getSearchResultOfSearchResult(searchResultOfClient);
//			searchResultList.stream().forEach(n -> new FileControler().deleteFile(n.filename + ".csv"));
                compareResultListTmp.stream().forEach(n -> new FileControler().deleteFile(n.myFileName + ".class-" + args[1] +".csv-" + n.filename + ".csv-" + args[1] + ".csv"));

                compareResultList.addAll(compareResultListTmp);
                compareResultStringList.addAll(compareResultStringListTmp);

            }else {
                CompareResultCreater creater = new CompareResultCreater(searchResultOfClient, birthmark, uploadFileOfClient + "-" + birthmark + ".csv");
                compareResultList = creater.getCompareResultOfCompareResult();
                compareResultStringList = creater.getCompareResultOfString();
//			List<SearchResult> searchResultList = new SearchResultCreater().getSearchResultOfSearchResult(searchResultOfClient);
//			searchResultList.stream().forEach(n -> new FileControler().deleteFile(n.filename + ".csv"));
                compareResultList.stream().forEach(n -> new FileControler().deleteFile(n.myFileName + ".class-"+ birthmark +".csv-" + n.filename + ".csv-" + birthmark + ".csv"));
            }
			
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
    public ModelAndView send(@RequestParam("upload")MultipartFile file,
							 @RequestParam("searchResult")String searchResultOfClient,
							 @RequestParam("birthmark") String birthmark,
							 @RequestParam("threshold") String threshold,
							 ModelAndView mav){
    	try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
			long start;
			long end;
			String rows = "100000000";//デフォルトを10にしておく
			String uploadFile = file.getOriginalFilename();
			List<String> searchResult = new ArrayList<>();
			List<SearchResult> searchResultList = new ArrayList<>();
    		if(Objects.equals(birthmark, "recomend")) {
                birthmark = "2-gram";
				new Extractor().extractBirthmark(file, birthmark);

				start = System.currentTimeMillis();
				searchResult = new SearchResultCreater(birthmark).getSearchResultOfString(file.getOriginalFilename(), rows, threshold);
//				end = System.currentTimeMillis();
				searchResultList = new SearchResultCreater(birthmark).getSearchResultOfSearchResult(searchResult);

                birthmark = "uc";
                new Extractor().extractBirthmark(file, birthmark);

//                start = System.currentTimeMillis();
                List<String> searchResultTmp2 = new SearchResultCreater(birthmark).getSearchResultOfString(file.getOriginalFilename(), rows, threshold);
                end = System.currentTimeMillis();
                searchResultTmp2.forEach(System.out::println);
                List<SearchResult> searchResultListTmp2 = new SearchResultCreater(birthmark).getSearchResultOfSearchResult(searchResultTmp2);

                searchResult.addAll(searchResultTmp2);
                searchResultList.addAll(searchResultListTmp2);

                birthmark = "2-gram,uc";

			}else {
				new Extractor().extractBirthmark(file, birthmark);

				start = System.currentTimeMillis();
				searchResult = new SearchResultCreater(birthmark).getSearchResultOfString(file.getOriginalFilename(), rows, threshold);
                searchResult.forEach(System.out::println);
				end = System.currentTimeMillis();
				searchResultList = new SearchResultCreater(birthmark).getSearchResultOfSearchResult(searchResult);
			}
//            List<String> classInformationList = new SearchResultCreater().getClassInformationList(searchResultList);
//            classInformationList.stream()
//					.forEach(System.out::println);
			List<String> compareResult = new ArrayList<>();
            
        	mav.setViewName("searchResult");
        	mav.addObject("note", String.join("\n", searchResult));
        	mav.addObject("uploadFile", uploadFile);
			mav.addObject("uploadFileName", uploadFile + "で検索中です");
        	mav.addObject("searchResult", String.join("\n", searchResult));
        	mav.addObject("searchResultList", searchResultList);
        	mav.addObject("note_js", String.join("\n", searchResult));
        	mav.addObject("uploadFile_js", uploadFile);
        	mav.addObject("birthmark", birthmark);
			mav.addObject("searchTime", "検索時間：" + (end - start) + "ms");
			mav.addObject("dataBaseNumber", "検索件数：" + searchResult.size());
        	mav.addObject("searchResult_js", String.join("\n", searchResult));
			mav.addObject("compareResult", String.join("\n", compareResult));
			mav.addObject("compareResult_js", String.join("\n", compareResult));
            return mav;
//        	mav.addObject("value", String.join("\n", searchResult));
        }catch(Exception e){
    	    e.printStackTrace();
        	mav.setViewName("search");
        	mav.addObject("errorMessageOfSearch", "対応可能な形式はclassとjarです");
            return mav;
        }
    }
}
