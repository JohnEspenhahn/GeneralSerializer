package org.espenhahn.serializer.specialvalueserializers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.espenhahn.serializer.huffman.HuffmanResult;
import org.espenhahn.serializer.huffman.util.ByteBufferInputStream;
import org.espenhahn.serializer.util.pool.ByteArrayWrapper;
import org.espenhahn.serializer.util.pool.ByteBufferWrapper;
import org.espenhahn.serializer.util.pool.ObjectInstantiator;
import org.espenhahn.serializer.util.pool.SortedPool;

public class ClassNameSerializerImpl implements ClassNameSerializer {
	private static SortedPool<ByteBufferWrapper> ByteBufferPool = new SortedPool<ByteBufferWrapper>(
			new ObjectInstantiator<ByteBufferWrapper>() {
				public ByteBufferWrapper newInstance(Object... args) {
					return new ByteBufferWrapper(ByteBuffer.allocate((int) args[0]));
				}
			});
	
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
//		if (encoder != null) {
//			ByteBufferWrapper bbw = ByteBufferPool.take(className.length()*2);
//			try {
//				bbw.bb.clear();
//				encoder.encode(className, bbw.bb, false);
//				bbw.bb.flip();
//				byte[] bytes = new byte[bbw.bb.limit()];
//				bbw.bb.get(bytes);
//				
//				// Test
//				String asString = new String(bytes);
//				bbw.bb.clear();
//				bbw.bb.order(ByteOrder.LITTLE_ENDIAN);
//				bbw.bb.put(asString.getBytes());
//				bbw.bb.flip();
//				String res = encoder.decode(new ByteBufferInputStream(bbw.bb));
//				System.out.println("In: " + className);
//				System.out.println("Out: " + res);
//				
//				
//				out.append(asString);
//				out.append('\0'); // Encoder shouldn't be able to generate \0 (0 is reserved as terminal)
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				ByteBufferPool.put(bbw);
//			}
//		} else
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
//			if (encoder != null) {
//				ByteBufferWrapper bbw = ByteBufferPool.take(1024);
//				bbw.bb.clear();
//				
//				int c;
//				while ((c = in.read()) != 0)
//					bbw.bb.putChar((char) c);
//				
//				bbw.bb.flip();
//				String res = encoder.decode(new ByteBufferInputStream(bbw.bb));
//				
//				ByteBufferPool.put(bbw);
//				
//				return res;
//			} else
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
