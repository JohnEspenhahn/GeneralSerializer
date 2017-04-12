package org.espenhahn.serializer.dispatchingserializer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.AValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({ Comp533Tags.BEAN_SERIALIZER })
public class BeanSerializerImpl extends AValueSerializer {

	@Override
	protected void objectToStringBuffer(StringBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof Serializable)) throw new NotSerializableException();
		emitBody(out, obj, visitedObjs);
	}

	@Override
	protected void objectToByteBuffer(ByteBuffer out, Object obj, VisitedObjects visitedObjs)
			throws NotSerializableException {
		if (!(obj instanceof Serializable)) throw new NotSerializableException();
		
		visitedObjs.visit(obj);
		out.putInt(RemoteReflectionUtility.listSize(obj));
		emitBody(out, obj, visitedObjs);
	}
	
	private void emitBody(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {		
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		try {
			BeanInfo info = Introspector.getBeanInfo(obj.getClass());
			
			visitedObjs.visit(obj);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			for (PropertyDescriptor d: descriptors) {
				if (d.getWriteMethod() == null) {
					continue; // Transient
				} else if (RemoteReflectionUtility.isTransient(d.getReadMethod())) {
					continue; // Transient
				} else {
					// Class<?> type = d.getReadMethod().getReturnType();
					try {
						// Get property value
						Object propVal = d.getReadMethod().invoke(obj);
						dispatcher.objectToBuffer(out, propVal, visitedObjs);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IntrospectionException e) {
			throw new NotSerializableException();
		}
	}

	@Override
	protected <T> T objectFromStringBuffer(StringBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs)
			throws StreamCorruptedException {
		if (!RemoteReflectionUtility.isList(clazz)) throw new IllegalArgumentException("Expected List, got " + clazz);
		
		// TODO
		throw new UnsupportedOperationException();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T objectFromByteBuffer(ByteBuffer in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
		DispatchingSerializer dispatcher = ValueSerializerRegistry.getDispatchingSerializer();
		
		try {
			Object obj = ValueSerializerRegistry.getDeserializedInstance(clazz);
			BeanInfo info = Introspector.getBeanInfo(clazz);
			
			retrievedObjs.save(obj);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
			for (PropertyDescriptor d: descriptors) {
				if (d.getWriteMethod() == null) {
					continue; // Transient
				} else if (RemoteReflectionUtility.isTransient(d.getReadMethod())) {
					continue; // Transient
				} else {
					try {
						d.getWriteMethod().invoke(obj, dispatcher.objectFromBuffer(in, retrievedObjs));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			
			return (T) obj;
		} catch (IntrospectionException | InstantiationException | IllegalAccessException e) {
			throw new StreamCorruptedException();
		}
	}

}
