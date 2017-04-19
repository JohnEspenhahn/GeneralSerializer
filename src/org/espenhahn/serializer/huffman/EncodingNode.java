package org.espenhahn.serializer.huffman;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.utils.BitInputStream;
import org.espenhahn.serializer.huffman.util.BitOutputStream;

public class EncodingNode extends HuffmanNode {
	private static final long serialVersionUID = -2465908902211308357L;
	public static boolean DEBUG = false;
	
	private transient int weight;
	
	private String label;
	private Map<Character, EncodingNode> edges;
	
	private int encoding;
	private byte bits;
	
	EncodingNode() {
		this("");
	}
	
	EncodingNode(String lbl) {
		this.edges = new HashMap<Character, EncodingNode>();
		this.label = lbl;
		this.weight = 0;
	}
	
	@Override
	public int getWeight() {
		return this.weight;
	}
	
	@Override
	public void setEncoding(int encoding, byte bits) {
		this.encoding = encoding;
		this.bits = bits;
		
		if (DEBUG) System.out.println(label + ": " + bits);
	}
	
	@Override
	public String decode(BitInputStream bis) throws IOException {
		return this.label;
	}
	
	public boolean encode(PushbackReader reader, BitOutputStream bos) throws IOException {
		int c = reader.read();
		if (c != -1) {
			EncodingNode next = edges.get((char) c);
			if (next != null) {
				next.encode(reader, bos);
				return true;
			} else {
				reader.unread(c);
				encode(bos);
				return true;
			}
		} else {
			encode(bos);
			return false;
		}		
	}
	
	private void encode(BitOutputStream bos) throws IOException {
		for (int i = 0; i < bits; i++) {
			int bit = (encoding >> i) & 1;
			bos.write(1, bit);
			
			if (DEBUG) System.out.print(bit == 1 ? '1' : '0');
		}
	}
	
	protected final void buildWeight(String s) {
		char next_c = s.charAt(0);
		EncodingNode next = edges.get(next_c);
		if (next == null) {
			next = new EncodingNode(label + next_c);
			edges.put(next_c, next);
		}
		
		next.weight += 1;
		if (s.length() > 1)
			next.buildWeight(s.substring(1));
	}
	
	protected final HuffmanNode[] flatten() {
		HuffmanNode[] arr = new HuffmanNode[countEdges()];
		flatten(arr, 0);
		return arr;
	}
	
	private final int countEdges() {
		int size = edges.size();
		for (EncodingNode n: edges.values())
			size += n.countEdges();
		return size;
	}
	
	private final int flatten(HuffmanNode[] arr, int idx) {
		for (EncodingNode n: edges.values()) {
			arr[idx++] = n;
			idx = n.flatten(arr, idx);
		}
		return idx;
	}
	
}
