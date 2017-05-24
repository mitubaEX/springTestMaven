package com.example.mituba.test.server;

import org.springframework.web.multipart.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.script.ScriptException;
import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;
//import com.google.common.io.Files;

public class Extractor{
    public void extractBirthmark(String fileOriginalName){
        try {
            ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
            ScriptRunner runner = builder.build();
            String[] arg = { "./extract.js", fileOriginalName};
			runner.runsScript(arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String createOriginalFile(MultipartFile file) throws IOException{
    	File saveFile = new File(file.getOriginalFilename());
        Files.copy(file.getInputStream(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file.getOriginalFilename();
    }

    public void createExtractFile(MultipartFile file){
        try {
			extractBirthmark(createOriginalFile(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
