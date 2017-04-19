package org.espenhahn.serializer.huffman;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.compress.utils.BitInputStream;
import org.espenhahn.serializer.huffman.util.BitOutputStream;
import org.espenhahn.serializer.huffman.util.ByteBufferInputStream;
import org.espenhahn.serializer.huffman.util.ByteBufferOutputStream;
import org.espenhahn.serializer.util.pool.Pool;

public class HuffmanResult implements Serializable {
	private static final long serialVersionUID = 7881273268974691493L;
	private static Pool<StringBuilder> StringBuilderPool = new Pool<StringBuilder>(StringBuilder.class);
	private static final int MAX_DEPTH = 3;
	
	private DecodingNode decode;
	private EncodingNode encode;
	
	private HuffmanResult(DecodingNode e, EncodingNode c) {
		this.decode = e;
		this.encode = c;
	}
	
	public void encode(String s, ByteBuffer bb) throws IOException {
		ByteBufferOutputStream bbos = new ByteBufferOutputStream();
		BitOutputStream bos = new BitOutputStream(bbos);
		
		StringReader sreader = new StringReader(s);
		PushbackReader reader = new PushbackReader(sreader);
		while (encode.encode(reader, bos)) { }
		bos.write(1, 0); // End marker
		// System.out.println();
		bos.close();		
		
		byte[] bytes = bbos.getBytes();
		bb.put(bytes, 0, bbos.getCount());
	}
	
	public String decode(ByteBufferInputStream bbis) throws IOException {
		BitInputStream bis = new BitInputStream(bbis, ByteOrder.BIG_ENDIAN);		
		
		StringBuilder decoded = StringBuilderPool.take();
		decoded.setLength(0);
		
		while (true) {
			String next = decode.decode(bis);
			if (next == null) break;
			else decoded.append(next);
		}
		// System.out.println();
		String res = decoded.toString();
		StringBuilderPool.put(decoded);
		return res;
	}
	
	public static HuffmanResult createFor(String[] ss) {
		EncodingNode root = new EncodingNode();
		// Count occurrences 
		// O(MAX_DEPTH * mn) = O(mn)
		for (String s: ss)
			for (int j = 0; j < MAX_DEPTH && j < s.length(); j++)
				for (int i = 0, ii = s.length() - j; i < ii; i++)
					root.buildWeight(s.substring(i, i+j+1));

		HuffmanNode[] all_nodes = root.flatten(); // O(mn)		
		MinHeap<HuffmanNode> heap = new MinHeap<HuffmanNode>(all_nodes); // Heapify O(log(mn))
		while (heap.heapsize() > 1) {
			HuffmanNode left = heap.removemin();
			HuffmanNode right = heap.removemin();
			heap.insert(new DecodingNode(left, right));
		}
		
		// Force 0 => empty
		DecodingNode encoding_root = new DecodingNode(new EncodingNode(null), (DecodingNode) heap.removemin());
		encoding_root.setEncoding(0, (byte) 0);
		
		return new HuffmanResult(encoding_root, root);
	}
	
}
