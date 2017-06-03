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
	public void extractBirthmark(MultipartFile file) throws IOException, ScriptException{
		ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
		ScriptRunner runner = builder.build();
		String[] arg = { "./extract.js", createOriginalFile(file)};
		runner.runsScript(arg);
	}

	public String createOriginalFile(MultipartFile file) throws IOException{
		File saveFile = new File(file.getOriginalFilename());
		Files.copy(file.getInputStream(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return file.getOriginalFilename();
	}
}
