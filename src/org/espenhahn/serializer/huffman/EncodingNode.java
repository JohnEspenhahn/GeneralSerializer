package org.espenhahn.serializer.huffman;

public class EncodingNode extends HuffmanNode {

	private HuffmanNode left;
	private HuffmanNode right;
	
	public EncodingNode(HuffmanNode left, HuffmanNode right) {
		this.left = left;
		this.right = right;
	}
	
	public String read(BitBuffer bb) {
		if (bb.getBit())
			return right.read(bb);
		else
			return left.read(bb);
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
	
	@Override
	public int getDepth() {
		return -1;
	}
	
}
