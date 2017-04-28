package org.espenhahn.serializer.specialvalueserializers;

import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.util.ByteArrayWrapper;
import org.espenhahn.util.pool.ObjectInstantiator;
import org.espenhahn.util.pool.Pool;
import org.espenhahn.util.pool.SortedPool;

public class StaticStringSerializer {
	public static final char SEPERATOR = ':';

	private static Pool<StringBuilder> StringBuilderPool = new Pool<StringBuilder>(StringBuilder.class);
	private static SortedPool<ByteArrayWrapper> ByteArrayPool = new SortedPool<ByteArrayWrapper>(
			new ObjectInstantiator<ByteArrayWrapper>() {
				public ByteArrayWrapper newInstance(Object... args) {
					return new ByteArrayWrapper((int) args[0]);
				}
			});
	
	public static void writeString(StringBuffer out, String str, char delim) {
		out.append(str);
		out.append(delim);
	}

	public static String readString(StringReader in, char delim) throws IOException {
		StringBuilder builder = StringBuilderPool.take();
		builder.setLength(0);

		while (true) {
			char c = (char) in.read();
			if (c == delim)
				break;
			else
				builder.append(c);
		}

		String str = builder.toString();
		StringBuilderPool.put(builder);

		return str;
	}

	public static void writeString(ByteBuffer out, String str) {
		byte[] bytes = str.getBytes();
		out.putInt(bytes.length);
		out.put(bytes);
	}

	public static void writeString(StringBuffer out, String str) {
		out.append(str.length());
		out.append(SEPERATOR);
		out.append(str);
	}

	public static String readString(ByteBuffer in) {
		int lng = in.getInt();
		ByteArrayWrapper arr = ByteArrayPool.take(lng);

		in.get(arr.getArray(), 0, lng);
		String str = new String(arr.getArray(), 0, lng);

		ByteArrayPool.put(arr);

		return str;
	}

	public static String readString(StringReader in) throws IOException {
		StringBuilder builder = StringBuilderPool.take();
		builder.setLength(0); // clear

		// Read length
		int lng;
		while (true) {
			char c = (char) in.read();
			if (!Character.isDigit(c)) {
				lng = Integer.parseInt(builder.toString());
				break;
			} else {
				builder.append(c);
			}
		}

		// Read string
		builder.setLength(0);
		for (int i = 0; i < lng; i++)
			builder.append((char) in.read());
		String str = builder.toString();

		StringBuilderPool.put(builder);

		return str;
	}
}
