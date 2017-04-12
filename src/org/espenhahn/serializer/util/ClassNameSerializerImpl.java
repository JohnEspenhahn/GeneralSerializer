package org.espenhahn.serializer.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ClassNameSerializerImpl implements ClassNameSerializer {
	public void writeClassName(Object out, String className) {
		if (out instanceof StringBuffer)
			writeClassName((StringBuffer) out, className);
		else if (out instanceof ByteBuffer)
			writeClassName((ByteBuffer) out, className);
		else
			throw new IllegalArgumentException("Expected buffer, got " + out);
	}
	
	public void writeClassName(StringBuffer out, String className) {
		out.append(className);
		out.append(':');
	}
	
	public void writeClassName(ByteBuffer out, String className) {
		try {			
			byte[] classNameBuff = className.getBytes("UTF8");
			if (classNameBuff.length > Short.MAX_VALUE) 
				throw new IllegalArgumentException("Classname longer than " + Short.MAX_VALUE);
			
			out.putShort((short) classNameBuff.length);
			out.put(classNameBuff);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String readClassName(Object in) {
		if (in instanceof StringBuffer)
			return readClassName((StringBuffer) in);
		else if (in instanceof ByteBuffer)
			return readClassName((ByteBuffer) in);
		else
			throw new IllegalArgumentException("Expected buffer, got " + in);
	}
	
	public String readClassName(StringBuffer in) {		
		// TODO
		throw new UnsupportedOperationException();
	}
	
	public String readClassName(ByteBuffer in) {
		int lng = in.getShort();
		byte[] className = new byte[lng];
		in.get(className);
		return new String(className);
	}
}
