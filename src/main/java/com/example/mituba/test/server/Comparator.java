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
			.map(n -> n.split(",",4))
			.filter(n -> n.length >= 4)
			.map(n -> compare(createFile(n[1], n[3], "2-gram"), kindOfBirthmark, uploadFile))
			.collect(Collectors.toList());
	}
	
	// csvファイルとして書き込む
	public void writeFile(String filename, String birthmark, File file, String kindOfBirthmark) throws IOException{
		FileWriter filewriter = new FileWriter(file);
		filewriter.write(filename.replace("/", ".")+",," + kindOfBirthmark + "," + birthmark);
		filewriter.close();
	}
	
	// csvファイルとして作成しwriteFileを呼ぶ
	public String createFile(String filename, String birthmark, String kindOfBirthmark){
		try{
			File file = new File(filename + ".csv");
			if(file.exists()){
				return filename + ".csv";
			}else{
				file.createNewFile();
				writeFile(filename, birthmark, file, kindOfBirthmark);
				return filename + ".csv";
			}
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}
	
	public String compare(String filename, String kindOfBirthmark, String uploadFile){
        try {
            ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
            ScriptRunner runner = builder.build();
            String[] arg = { "./compare_input_csv_test.js", kindOfBirthmark, uploadFile, filename };
			runner.runsScript(arg);
			return uploadFile.replace("/", ".") + "-" + filename.replace("/", ".") + "-" + kindOfBirthmark + ".csv";  
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