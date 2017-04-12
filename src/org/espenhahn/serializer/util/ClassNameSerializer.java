package org.espenhahn.serializer.util;

import java.nio.ByteBuffer;

public interface ClassNameSerializer {
	void writeClassName(Object out, String className);
	
	void writeClassName(StringBuffer out, String className);
	
	void writeClassName(ByteBuffer out, String className);
	
	String readClassName(Object in);
	
	String readClassName(StringBuffer in);
	
	String readClassName(ByteBuffer in);
}
