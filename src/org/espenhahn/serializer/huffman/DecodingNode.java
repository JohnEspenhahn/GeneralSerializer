package org.espenhahn.serializer.huffman;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.compress.utils.BitInputStream;

public class DecodingNode extends HuffmanNode implements Serializable {
	private static final long serialVersionUID = -5118526191862640551L;

	public static boolean DEBUG = false;

	private HuffmanNode left;
	private HuffmanNode right;
	
	public DecodingNode(HuffmanNode left, HuffmanNode right) {
		this.left = left;
		this.right = right;
	}
	
	public String decode(BitInputStream bis) throws IOException {
		long bit = bis.readBits(1);
		if (bit == -1) return null; // End of stream

		if (DEBUG) System.out.print(bit == 1 ? '1' : '0');
		
		if (bit == 1)
			return right.decode(bis);
		else
			return left.decode(bis);
	}
	
	@Override
	public void setEncoding(int encoding, byte bits) {
		if (bits >= 30) throw new RuntimeException("Too many bits to encode!");
		
		left.setEncoding(encoding, (byte) (bits+1));
		
		encoding |= 1 << bits;
		right.setEncoding(encoding, (byte) (bits+1));
	}

	@Override
	public int getWeight() {
		return left.getWeight() + right.getWeight();
	}
	
}
