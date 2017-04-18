package org.espenhahn.serializer.huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanBuilder {

	public static void main(String[] args) {
		File f = new File("items.dat");
		try {
			ArrayList<String> strs = new ArrayList<String>();
			Scanner s = new Scanner(new FileInputStream(f));
			
			String line = s.nextLine();
			for (String li: line.split("/"))
				strs.add(li);
			
			CharNode.createFor(strs.toArray(new String[strs.size()]));
			
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
