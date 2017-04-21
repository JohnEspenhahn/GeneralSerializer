package org.espenhahn.serializer.huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import org.espenhahn.serializer.huffman.util.ByteBufferInputStream;

public class HuffmanBuilder {

	public static void main(String[] args) throws IOException {
		String input = "huffmansources/input.txt";
		String output = "550_huffman.dat";
		boolean addTerminal = false;
		
		DecodingNode.DEBUG = true;
		EncodingNode.DEBUG = true;
		
		File f = new File(input);
		try {
			ArrayList<String> inList = new ArrayList<String>();
			Scanner scanner = new Scanner(new FileInputStream(f));
			while (scanner.hasNext())
				inList.add(scanner.nextLine());
			scanner.close();
			
			String[] strings = inList.toArray(new String[inList.size()]);
			HuffmanResult res = HuffmanResult.createFor(strings, addTerminal);
			
			// Test
			final int start = 0;
			final int tests = inList.size();
			ByteBuffer bb = ByteBuffer.allocate(8192);
			ByteBufferInputStream bbis = new ByteBufferInputStream(bb);
			double savings = 0;
			for (int i = start; i < start+tests; i++) {
				String s = inList.get(i);
				
				bb.clear();
				res.encode(s, bb, addTerminal);
				bb.flip();
				
				double originalLng = s.length();
				double encodedLng = bb.limit();
				double thisSavings = (originalLng - encodedLng)/originalLng;
				savings += thisSavings;
				
				System.out.println("Input   = " + s);
				System.out.println("Decoded = " + res.decode(bbis));
				System.out.println("Savings = " + thisSavings);
			}
			
			System.out.printf("Avg savings of %.1f%%\n", (savings/tests)*100);
			
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(output)));
			os.writeObject(res);
			os.close();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e);
		}
	}
	
}
