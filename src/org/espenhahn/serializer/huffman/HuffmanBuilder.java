package org.espenhahn.serializer.huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanBuilder {

	public static void main(String[] args) throws IOException {
		File f = new File("classes.dat");
		try {
			ArrayList<String> strs = new ArrayList<String>();
			Scanner s = new Scanner(new FileInputStream(f));
			
			while (s.hasNext()) {
				strs.add(s.nextLine());
			}
			
			HuffmanResult res = CharNode.createFor(strs.toArray(new String[strs.size()]));
			
			s.close();
			
			// Test
			BitBuffer out = new BitBuffer(ByteBuffer.allocate(20));
			
			String test = "java.lang.String";
			StringReader reader = new StringReader(test);
			while (res.c.encode(reader, out)) { }
			
			System.out.println("Original length = " + test.getBytes("UTF8").length);
			System.out.println("Encoded length = " + out.limit());
			
			out.flip();
			StringBuilder decoded = new StringBuilder();
			while (out.bb.remaining() > 0) {
				decoded.append(res.e.read(out));
			}
			
			System.out.println("Decoded = " + decoded.toString());
			
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e);
		}
	}
	
}
