package org.espenhahn.serializer.util.pool;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
	private final int MAX_BUFFERS = 20;
	
	private List<T> pool;
	private ObjectInstantiator<T> instantiator;
	
	
	public Pool(Class<T> clazz) {
		this(new ObjectInstantiator<T>() {

			@Override
			public T newInstance(Object... args) {
				try {
					return clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					return null;
				}
			}
		
		});
	}
	
	public Pool(ObjectInstantiator<T> instantiator) {
		this.pool = new ArrayList<T>(MAX_BUFFERS);
		this.instantiator = instantiator;
	}

	/**
	 * Take an object from the pool
	 * @return An object that isn't in use
	 */
	public synchronized T take() {
		if (pool.isEmpty())
			return instantiator.newInstance();
		else
			return pool.remove(pool.size()-1);
	}
	
	/**
	 * Give the object back to the pool
	 * @param obj The object
	 * @return True if the pool took it, false if the pool is full
	 */
	public synchronized boolean put(T obj) {
		if (this.pool.size() < MAX_BUFFERS) {
			this.pool.add(obj);
			return true;
		} else {
			return false;
		}
	}
	
}
