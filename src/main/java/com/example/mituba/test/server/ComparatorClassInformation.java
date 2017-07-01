package com.example.mituba.test.server;

/**
 * Created by mituba on 2017/06/22.
 */
public class ComparatorClassInformation {
    public String filePath;
    public String filename;
    public String classInformation;
    public String jar;
    public String groupID;
    public String artifactID;
    public String version;
    public ComparatorClassInformation(String filePath, String jar ,String groupID, String artifactID, String version){
        this.filePath = filePath;
        this.filename = filename;
        this.groupID = groupID;
        this.artifactID = artifactID;
        this.version = version;
        this.jar = jar;
    }

    public String getFilePath(){
        return filePath;
    }
    public String getGroupID(){
        return groupID;
    }
    public String getArtifactID(){
        return artifactID;
    }
    public String getVersion(){
        return version;
    }
    public String getJar(){
        return jar;
    }
}
