package org.espenhahn.serializer.huffman;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.compress.utils.BitInputStream;

public abstract class HuffmanNode implements Comparable<HuffmanNode>, Serializable {
	private static final long serialVersionUID = 1107838257574243005L;

	public abstract int getWeight();
	
	public abstract void setEncoding(int encoding, byte bits);
	public abstract String decode(BitInputStream bit) throws IOException;
	
	@Override
	public int compareTo(HuffmanNode other) {
		if (other == this)
			return 0;
		else if (other.getWeight() <= getWeight())
			return 1;
		else
			return -1;
	}
	
}
