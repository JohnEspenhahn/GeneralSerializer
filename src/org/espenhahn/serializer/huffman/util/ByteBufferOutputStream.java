package org.espenhahn.serializer.huffman.util;

import java.io.ByteArrayOutputStream;

public class ByteBufferOutputStream extends ByteArrayOutputStream {

	public ByteBufferOutputStream() { }

	public ByteBufferOutputStream(int size) {
		super(size);
	}
	
	public int getCount() {
		return count;
	}

	public byte[] getBytes() {
		return this.buf;
//		ByteBuffer bb = ByteBuffer.allocate(count);
//		bb.order(ByteOrder.LITTLE_ENDIAN);
//		bb.put(this.buf, 0, count);
//		bb.rewind();
//		return bb;
	}

}
