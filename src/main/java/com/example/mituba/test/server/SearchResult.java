package com.example.mituba.test.server;

public class SearchResult{
    public String filename;
    public Double risk;
    public Double riskNum;
    public String myFileName;

    public SearchResult(String myFileName, String filename, Double risk){
        this.filename = filename;
        this.risk = risk;
        this.myFileName = myFileName;
        // if(risk >= 0.75)
        //     this.risk = "やばい";
        // else if(0.5 <= risk && risk < 0.75)
        //     this.risk = "ちょっちやばい";
        // if(risk < 0.5)
        //     this.risk = "無害";
        this.riskNum = risk;
    }
}
