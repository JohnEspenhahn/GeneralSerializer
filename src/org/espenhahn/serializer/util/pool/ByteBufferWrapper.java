package org.espenhahn.serializer.util.pool;

import java.nio.ByteBuffer;

public class ByteBufferWrapper implements Sizable {
	public final ByteBuffer bb;
	
	public ByteBufferWrapper(ByteBuffer bb) {
		this.bb = bb;
	}
	
	@Override
	public int getSize() {
		return bb.capacity();
	}

	
	
}
