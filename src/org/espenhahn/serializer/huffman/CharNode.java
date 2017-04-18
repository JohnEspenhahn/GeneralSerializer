package org.espenhahn.serializer.huffman;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class CharNode extends HuffmanNode {
	private static final int MAX_DEPTH = 4;

	private int depth;
	private String label;
	private Map<Character, CharNode> edges;
	private int weight;
	
	private int encoding;
	private byte bits;
	
	private CharNode() {
		this("",0);
	}
	
	public CharNode(String lbl, int depth) {
		this.edges = new HashMap<Character, CharNode>();
		this.depth = depth;
		this.label = lbl;
		this.weight = 0;
	}
	
	@Override
	public int getWeight() {
		return this.weight;
	}
	
	@Override
	public int getDepth() {
		return depth;
	}
	
	@Override
	public void setEncoding(int encoding, byte bits) {
		this.encoding = encoding;
		this.bits = bits;
		
		System.out.println(label + ": " + bits);
	}
	
	@Override
	public String read(BitBuffer bb) {
		return this.label;
	}
	
	public boolean encode(StringReader sr, BitBuffer bb) throws IOException {
		int c = sr.read();
		if (c == -1) return false;		
		
		CharNode next = edges.get((char) c);
		if (next == null) {
			for (int i = 0; i < bits; i++) {
				int bit = (encoding >> i) & 1;
				if (bit == 1) bb.putBit1();
				else bb.putBit0();
			}
		} else {
			next.encode(sr, bb);
		}
		
		return true;
	}
	
	private void visit(String s) {
		char next_c = s.charAt(0);
		CharNode next = edges.get(next_c);
		if (next == null) {
			next = new CharNode(label + next_c, depth+1);
			edges.put(next_c, next);
		}
		
		next.weight += 1;
		if (s.length() > 1)
			next.visit(s.substring(1));
	}
	
	private int countEdges() {
		int size = edges.size();
		for (CharNode n: edges.values())
			size += n.countEdges();
		return size;
	}
	
	private int flatten(HuffmanNode[] arr, int idx) {
		for (CharNode n: edges.values()) {
			arr[idx++] = n;
			idx = n.flatten(arr, idx);
		}
		return idx;
	}
	
	public static HuffmanResult createFor(String[] ss) {
		CharNode root = new CharNode();
		// Count occurrences 
		// O(MAX_DEPTH * n) = O(n)
		for (String s: ss) {
			for (int j = 1; j <= MAX_DEPTH; j++) {
				for (int i = 0, ii = s.length() - (j+1); i < ii; i++) {
					root.visit(s.substring(i, i+j));
				}
			}
		}
		
		HuffmanNode[] all_nodes = new HuffmanNode[root.countEdges()];
		root.flatten(all_nodes, 0);
		
		MinHeap<HuffmanNode> heap = new MinHeap<HuffmanNode>(all_nodes);
		while (heap.heapsize() > 1) {
			HuffmanNode left = heap.removemin();
			HuffmanNode right = heap.removemin();
			heap.insert(new EncodingNode(left, right));
		}
		
		EncodingNode encoding_root = (EncodingNode) heap.removemin();
		encoding_root.setEncoding(0, (byte) 0);
		
		return new HuffmanResult(encoding_root, root);
	}
	
}
