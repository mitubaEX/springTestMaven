package com.example.mituba.test.server;

public class SearchResult{
    public String filename;
    public Double risk;
    public Double riskNum;
    public String myFileName;
    public String jar;

    public SearchResult(String myFileName, String filename, Double risk, String jar){
        this.filename = filename;
        this.risk = risk;
        this.myFileName = myFileName;
        this.jar = jar;
        // if(risk >= 0.75)
        //     this.risk = "やばい";
        // else if(0.5 <= risk && risk < 0.75)
        //     this.risk = "ちょっちやばい";
        // if(risk < 0.5)
        //     this.risk = "無害";
        this.riskNum = risk;
    }
}
