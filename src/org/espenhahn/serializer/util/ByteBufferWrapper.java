package org.espenhahn.serializer.util;

import java.nio.ByteBuffer;

import org.espenhahn.util.pool.Sizable;

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
