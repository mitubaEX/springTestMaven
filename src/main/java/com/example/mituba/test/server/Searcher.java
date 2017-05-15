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

    public SolrQuery createQuery(String birthmark, String fl, String rows, String sort){
        SolrQuery query = new SolrQuery().setSort(sort, SolrQuery.ORDER.desc);
        query.set("q", birthmark);
        query.set("fl", fl);
        query.set("rows", rows);
        return query;
    }

    public SolrDocumentList getSolrDocumentList() throws UnsupportedEncodingException, SolrServerException, IOException{
        return new HttpSolrClient.Builder("http://localhost:"+portNum+"/solr/#/"+kindOfBirthmark).build()
            .query(createQuery(birthmark, "filename, lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit), data", rows, "strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)"))
            .getResults();
    }

    public List<String> searchPerform(){
        try{
        	List<String> list = new ArrayList<>();
//            SolrDocumentList solrDoc = getSolrDocumentList();
        	Process r = Runtime.getRuntime().exec("curl http://localhost:"+portNum+"/solr/" + kindOfBirthmark + "/query"
                + "?fl=filename,lev:strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8")+"\",edit),data&sort="+"strdist(data,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)+desc"+" -d {"
                + "params:{"
                + "q:\""+birthmark.replace(" ", "+")+"\","
                + "rows:"+rows+","
                + "wt:\"csv\""
                + "}"
                + "}");
        	new BufferedReader(new InputStreamReader(r.getInputStream())).lines()
        			.map(n -> n.split(",",3))
                    .filter(i -> i.length >= 3  && !Objects.equals(i[1], "lev"))
                    .forEach(n -> list.add(n[0] + "," + n[1] + n[2]));
//            for(SolrDocument doc : solrDoc){
//                // if((float)doc.get("lev") <= threshold)
//                //     break;
//                list.add(doc.get("filename") + "," + doc.get("lev") + "," + doc.get("data"));
//            }
            return list;
        }catch(Exception e){
            System.out.println(e + ":solrj");
            return new ArrayList<>();
        }

    }
}
