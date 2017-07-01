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
	public List<ComparatorClassInformation> getCompareResult(List<String> list, String kindOfBirthmark, String uploadFile){
		return list.stream()
			.map(n -> n.split(",",8))
			.filter(n -> n.length >= 8)
			.map(n -> compare(createFile(n[1], n[7], kindOfBirthmark, n[3])
					, kindOfBirthmark, uploadFile, n[3], n[4], n[5], n[6]))
			.collect(Collectors.toList());
	}
	
	// csvファイルとして書き込む
	public void writeFile(String filename, String birthmark, File file, String kindOfBirthmark, String jar) throws IOException{
		FileWriter filewriter = new FileWriter(file);
		filewriter.write(filename.replace("/", ".")+","+ jar +"," + kindOfBirthmark + "," + birthmark.replace("-", "."));
		filewriter.close();
	}
	
	// csvファイルとして作成しwriteFileを呼ぶ
	public String createFile(String filename, String birthmark, String kindOfBirthmark, String jar){
		try{
			String filenameCSV = filename + ".csv";
			File file = new File(filenameCSV);
			if(file.exists()){
				new FileControler().deleteFile(filenameCSV);
				file.createNewFile();
				writeFile(filename, birthmark, file, kindOfBirthmark, jar);
				return filenameCSV;
			}else{
				file.createNewFile();
				writeFile(filename, birthmark, file, kindOfBirthmark, jar);
				return filenameCSV;
			}
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
	}
	
	public ComparatorClassInformation compare(String filename, String kindOfBirthmark, String uploadFile, String jar,String groupID, String artifactID, String version){
        try {
            ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
            ScriptRunner runner = builder.build();
            String[] arg = { "./compare_input_csv_test.js", kindOfBirthmark, uploadFile, filename };
			runner.runsScript(arg);
			new FileControler().deleteFile(filename);
			return new ComparatorClassInformation(uploadFile.replace("/", ".") + "-" + filename.replace("/", ".") + "-" + kindOfBirthmark + ".csv",
					jar,groupID, artifactID, version);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ComparatorClassInformation("","","","", "");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ComparatorClassInformation("","","","", "");
		}
	}
}