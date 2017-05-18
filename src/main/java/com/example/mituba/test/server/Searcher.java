package com.example.mituba.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class Searcher{
    private String birthmark;
    private String portNum;
    private String kindOfBirthmark;
    private String rows;
    private Double threshold;

    public Searcher(String birthmark, String portNum, String kindOfBirthmark, String rows, Double threshold){
        this.birthmark = birthmark;
        this.portNum = portNum;
        this.kindOfBirthmark = kindOfBirthmark;
        this.rows = rows;
        this.threshold = threshold;
    }

    
    public Process getCurlProcess(){
        try {
            return Runtime.getRuntime().exec("curl http://localhost:"+portNum+"/solr/" + kindOfBirthmark + "/query"
                + "?fl=filename,lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8")+"\",edit),data&sort="+"strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)+desc"+" -d {"
                + "params:{"
                + "q:\""+birthmark.replace(" ", "+")+"\","
                + "rows:"+rows+","
                + "wt:\"csv\""
                + "}"
                + "}");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public List<String> searchPerform(){
        try{
        	List<String> list = new ArrayList<>();
        	new BufferedReader(new InputStreamReader(getCurlProcess().getInputStream())).lines()
        			.map(n -> n.split(",",3))
                    .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev"))
                    .forEach(n -> list.add(n[0] + "," + n[1] + n[2]));
            return list;
        }catch(Exception e){
            System.out.println(e + ":solrj");
            return new ArrayList<>();
        }

    }
}
