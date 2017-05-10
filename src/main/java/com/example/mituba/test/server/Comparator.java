package com.example.mituba.test.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.script.ScriptException;
import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

public class Comparator {
	public List<String> getCompareResult(List<String> list, String kindOfBirthmark){
		list.stream()
			.map(n -> n.split(",",3))
			.forEach(n -> compare(n[0] ,n[1], n[2], kindOfBirthmark));
		return null;
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
	
	public void compare(String filename, String lev, String birthmark, String kindOfBirthmark){
		createFile(filename, birthmark);
        ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
        ScriptRunner runner = builder.build();
        String[] arg = { "./compare_input_csv_test.js", kindOfBirthmark, "test.txt", filename + ".csv" };
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
}