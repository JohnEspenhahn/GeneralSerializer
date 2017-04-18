package org.espenhahn.serializer.huffman.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends ByteArrayOutputStream {

	public ByteBufferOutputStream() { }

	public ByteBufferOutputStream(int size) {
		super(size);
	}

	public ByteBuffer getByteBuffer() {
		ByteBuffer bb = ByteBuffer.allocate(count);
		bb.put(this.buf, 0, count);
		return bb;
	}

}
