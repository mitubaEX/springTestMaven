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
    private List<String> searchResult = new ArrayList<>();

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

            createFile(file);

            ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
            ScriptRunner runner = builder.build();
            String[] arg = { "./extract.js", "./test.jar"};
            runner.runsScript(arg);

            // System.out.println("hello!");
            // System.out.println(rows);//何も入力しないと""
            if(Objects.equals(rows, ""))//何も入力されなかったら，100件検索する．
                rows = "100";

            // readList = readFile(new BufferedReader(new FileReader(new File("./test.txt"))));
            // readList.stream()
            //     .forEach(n -> searchPerform("8982", "2gram", rows, n[2], 0.75));
        	// mav.addObject("name", "hello");
        	mav.addObject("note", String.join("\n", searchResult));
        	mav.addObject("value", String.join("\n", searchResult));
        	mav.setViewName("index");
        }catch(Exception e){

        }
        return mav;
    }

    public void createFile(MultipartFile file) throws FileNotFoundException, IOException{
        File saveFile = new File("./test.jar");
        file.transferTo(saveFile);
    }

    public SolrQuery createQuery(String birthmark, String fl, String rows, String sort){
        SolrQuery query = new SolrQuery().setSort(sort, SolrQuery.ORDER.desc);
        query.set("q", birthmark);
        query.set("fl", fl);
        query.set("rows", rows);
        return query;
    }

    public void searchPerform(String portNum, String kindOfBirthmark, String coreNum, String birthmark, Double threshold){
        try{
            SolrClient server = new HttpSolrClient.Builder("http://localhost:"+portNum+"/solr/birth_"+kindOfBirthmark+""+coreNum).build();
            SolrQuery query = createQuery(birthmark, "filename, lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit), data", "10", "strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)");
            QueryResponse response = server.query(query);
            SolrDocumentList list = response.getResults();
            for(SolrDocument doc : list){
                // if((float)doc.get("lev") <= threshold)
                //     break;
                // System.out.println(doc.get("filename").toString() + ":" + doc.get("lev").toString() + ":" + doc.get("data").toString());
                searchResult.add(doc.get("filename").toString() + ":" + doc.get("lev").toString() + ":" + doc.get("data").toString());
                // System.out.println(doc.get("filename") + "," + doc.get("lev") + "," + doc.get("data"));
            }
        }catch(Exception e){
            System.out.println(e + ":solrj");
        }

    }

    public List<String[]> readFile(BufferedReader br){
    	return br.lines()
            .map(n -> n.split(",",3))
            .filter(n -> n.length >= 3)
            .collect(Collectors.toList());
    }
}
