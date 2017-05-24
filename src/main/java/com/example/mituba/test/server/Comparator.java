package com.example.mituba.test.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.ScriptException;
import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

public class Comparator {
	public List<String> getCompareResult(List<String> list, String kindOfBirthmark, String uploadFile){
		return list.stream()
			.map(n -> n.split(",",3))
			.filter(n -> n.length >= 3)
			.map(n -> compare(n[0] ,n[1], n[2], kindOfBirthmark, uploadFile))
			.collect(Collectors.toList());
	}
	
	public void writeFile(String filename, String birthmark, File file) throws IOException{
		FileWriter filewriter = new FileWriter(file);
		filewriter.write(filename+",,2-gram," + birthmark);
		filewriter.close();
	}
	
	public void createFile(String filename, String birthmark){
		File file = new File(filename + ".csv");
		if(!file.exists()){
			try {
				file.createNewFile();
                writeFile(filename, birthmark, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String compare(String filename, String lev, String birthmark, String kindOfBirthmark, String uploadFile){
        try {
            createFile(filename, birthmark);
            ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
            ScriptRunner runner = builder.build();
            String[] arg = { "./compare_input_csv_test.js", kindOfBirthmark, uploadFile + ".csv", filename + ".csv" };
			runner.runsScript(arg);
			return uploadFile + ".csv-" + filename + ".csv-" + kindOfBirthmark + ".csv";  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}