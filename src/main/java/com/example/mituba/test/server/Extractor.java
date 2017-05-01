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

public class Extractor{
    public void extractBirthmark(){
        ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
        ScriptRunner runner = builder.build();
        String[] arg = { "./extract.js", "./test.jar"};
        runner.runsScript(arg);
    }

    public void createExtractFile(MultipartFile file) throws FileNotFoundException, IOException{
        File saveFile = new File("./test.jar");
        file.transferTo(saveFile);
        extractBirthmark();
    }
}
