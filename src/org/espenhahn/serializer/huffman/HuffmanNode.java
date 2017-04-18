package org.espenhahn.serializer.huffman;

public abstract class HuffmanNode implements Comparable<HuffmanNode> {

	public abstract int getWeight();
	public abstract int getDepth();
	
	public abstract void setEncoding(int encoding, byte bits);
	
	@Override
	public int compareTo(HuffmanNode other) {
		if (other == this)
			return 0;
		else if (other.getWeight() <= getWeight())
			return -1;
		else
			return 1;
	}
	
}
