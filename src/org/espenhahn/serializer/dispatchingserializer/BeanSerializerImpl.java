package org.espenhahn.serializer.dispatchingserializer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;

import org.espenhahn.serializer.ValueSerializerRegistry;
import org.espenhahn.serializer.util.RetrievedObjects;
import org.espenhahn.serializer.util.VisitedObjects;
import org.espenhahn.serializer.valueserializers.ValueSerializer;

import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({ Comp533Tags.BEAN_SERIALIZER })
public class BeanSerializerImpl implements ValueSerializer {

	@Override
	public boolean isTerminal() {
		return false;
	}
	
	@Override
	public void objectToBuffer(Object out, Object obj, VisitedObjects visitedObjs) throws NotSerializableException {
		if (!(obj instanceof Serializable)) throw new NotSerializableException();
		
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
					try {
						// Get property value
						Object propVal = d.getReadMethod().invoke(obj);
						
						// Terminal values can be serialized without class
						ValueSerializer vs = null;
						if (propVal != null)
							vs = ValueSerializerRegistry.getValueSerializer(propVal.getClass());
						
						if (vs != null && vs.isTerminal())
							vs.objectToBuffer(out, propVal, visitedObjs);
						else
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
	@SuppressWarnings("unchecked")
	public <T> T objectFromBuffer(Object in, Class<T> clazz, RetrievedObjects retrievedObjs) throws StreamCorruptedException {
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
						Class<?> type = d.getPropertyType();
						
						// Terminal values can be deserialized without reading class
						ValueSerializer vs = ValueSerializerRegistry.getValueSerializer(type);
						if (vs != null && vs.isTerminal())
							d.getWriteMethod().invoke(obj, vs.objectFromBuffer(in, type, retrievedObjs));
						else
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
