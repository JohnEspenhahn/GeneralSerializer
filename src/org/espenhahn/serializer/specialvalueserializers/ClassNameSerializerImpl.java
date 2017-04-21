package org.espenhahn.serializer.specialvalueserializers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.huffman.HuffmanResult;
import org.espenhahn.serializer.huffman.util.ByteBufferInputStream;

public class ClassNameSerializerImpl implements ClassNameSerializer {	
	private static final char DELIM = ':';
	private HuffmanResult encoder;
	
	public ClassNameSerializerImpl() { }
	
	public ClassNameSerializerImpl(File f) throws IOException {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			encoder = (HuffmanResult) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		} finally {
			if (ois != null)
				ois.close();
		}
	}
	
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
		if (encoder != null) {
			try {
				encoder.encode(className, out, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { 
			StaticStringSerializer.writeString(out, className);
		}
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
		if (encoder != null)
			try {
				return encoder.decode(new ByteBufferInputStream(in));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		else
			return StaticStringSerializer.readString(in);
	}
}
