package com.example.mituba.test.server;

public class CompareResult{
    public String filename;
    public Double risk;
    public Double riskNum;
    public String myFileName;
    public String groupID;
    public String artifactID;
    public String version;
    public String classInformation;
    public String jar;

    public CompareResult(String myFileName, String filename, Double risk, String jar, String groupID, String artifactID, String version){
        this.filename = filename;
        this.risk = risk;
        this.myFileName = myFileName;
        this.groupID = groupID;
        this.artifactID = artifactID;
        this.version = version.replace("_", ".");
        this.jar = jar;
        this.classInformation = groupID + "," + artifactID + "," + version.replace("_", ".");
        // if(risk >= 0.75)
        //     this.risk = "やばい";
        // else if(0.5 <= risk && risk < 0.75)
        //     this.risk = "ちょっちやばい";
        // if(risk < 0.5)
        //     this.risk = "無害";
        this.riskNum = risk;
    }
}
