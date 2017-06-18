package com.example.mituba.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Searcher{
    private String birthmark;
    private String portNum;
    private String kindOfBirthmark;
    private String rows;
    private String myFileName;

    public Searcher(String myFileName, String birthmark, String portNum, String kindOfBirthmark, String rows){
        this.birthmark = birthmark;
        this.portNum = portNum;
        this.kindOfBirthmark = kindOfBirthmark;
        this.rows = rows;
        this.myFileName = myFileName;
    }


    public Process getCurlProcess(){
        try {
            return Runtime.getRuntime().exec("curl http://localhost:"+portNum+"/solr/" + kindOfBirthmark + "/query"
                + "?fl=filename,lev:strdist(birthmark,\"" + URLEncoder.encode(birthmark, "UTF-8")+"\",edit),jar,groupID,artifactID,version,birthmark&sort="+"strdist(birthmark,\"" + URLEncoder.encode(birthmark, "UTF-8") + "\",edit)+desc"+" -d {"
                + "params:{"
                + "q:\""+birthmark.replace(" ", "+").replace(".", "-")+"\","
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
        	return new BufferedReader(new InputStreamReader(getCurlProcess().getInputStream())).lines()
        			.map(n -> n.split(",",7))
                    .filter(i -> i.length >= 7  && !Objects.equals(i[1], "lev"))
                    .map(n -> myFileName + "," + n[0] + "," + n[1] + "," + n[2] + "," + n[3]
                            + "," + n[4] + "," + n[5] + "," + n[6])
                    .collect(Collectors.toList());
        }catch(Exception e){
            System.out.println(e + ":solrj");
            return new ArrayList<>();
        }

    }
}
