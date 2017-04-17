package org.espenhahn.serializer.util;

import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;

public class ClassNameSerializerImpl implements ClassNameSerializer {
	private static final char DELIM = ':';
	
	public void writeClassName(Object out, String className) {
		if (out instanceof StringBuffer)
			writeClassName((StringBuffer) out, className);
		else if (out instanceof ByteBuffer)
			writeClassName((ByteBuffer) out, className);
		else
			throw new IllegalArgumentException("Expected buffer, got " + out);
	}
	
	public void writeClassName(StringBuffer out, String className) {
		StaticStringSerializer.writeString(out, className, DELIM);
	}
	
	public void writeClassName(ByteBuffer out, String className) {
		StaticStringSerializer.writeString(out, className);
	}
	
	public String readClassName(Object in) {
		if (in instanceof StringReader)
			return readClassName((StringReader) in);
		else if (in instanceof ByteBuffer)
			return readClassName((ByteBuffer) in);
		else
			throw new IllegalArgumentException("Expected buffer, got " + in);
	}
	
	public String readClassName(StringReader in) {		
		try {
			return StaticStringSerializer.readString(in, DELIM);
		} catch (IOException e) {
			return null;
		}
	}
	
	public String readClassName(ByteBuffer in) {
		return StaticStringSerializer.readString(in);
	}
}
