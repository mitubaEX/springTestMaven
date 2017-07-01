package com.example.mituba.test.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FileControler {
	public void deleteFile(String filename){
		File file = new File(filename);
		file.delete();
	}

	public List<String> readFileList(String filename) throws FileNotFoundException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		List<String> list = br.lines().collect(Collectors.toList());
		br.close();
		return list;
	}
	
	public String readFile(String filename){
		try {
			String str = String.join("\n", readFileList(filename));
			return str;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
