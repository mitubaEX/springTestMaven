package com.example.mituba.test.server;

import org.springframework.web.multipart.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.script.ScriptException;
import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

public class Extractor{
    public void extractBirthmark(){
        ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
        ScriptRunner runner = builder.build();
        String[] arg = { "./extract.js", "./test.jar"};
        try {
			runner.runsScript(arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void createExtractFile(MultipartFile file) throws FileNotFoundException, IOException{
        File saveFile = new File("./test.jar");
        file.transferTo(saveFile);
        extractBirthmark();
    }
}
