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
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.http.*;

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

    public SolrQuery createQuery(String birthmark, String fl, String rows, String sort){
        SolrQuery query = new SolrQuery().setSort(sort, SolrQuery.ORDER.desc);
        query.set("q", birthmark);
        query.set("fl", fl);
        query.set("rows", rows);
        return query;
    }

    public SolrDocumentList getSolrDocumentList() throws UnsupportedEncodingException, SolrServerException, IOException{
        return new HttpSolrClient.Builder("http://localhost:"+portNum+"/solr/"+kindOfBirthmark).build()
            .query(createQuery(birthmark, "filename, lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit), data", rows, "strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)"))
            .getResults();
    }

    public List<String> searchPerform(){
        try{
            SolrDocumentList solrDoc = getSolrDocumentList();
            for(SolrDocument doc : solrDoc){
                // if((float)doc.get("lev") <= threshold)
                //     break;
                list.add(doc.get("filename") + "," + doc.get("lev") + "," + doc.get("data"));
            }
            return list;
        }catch(Exception e){
            System.out.println(e + ":solrj");
            return null;
        }

    }
}
