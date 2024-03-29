package org.espenhahn.serializer.util;

import org.espenhahn.util.pool.Sizable;

public class ByteArrayWrapper implements Sizable {

	private byte[] arr;
	
	public ByteArrayWrapper(int size) {
		arr = new byte[size];
	}
	
	@Override
	public int getSize() {
		return arr.length;
	}
	
	public byte[] getArray() {
		return arr;
	}

}
