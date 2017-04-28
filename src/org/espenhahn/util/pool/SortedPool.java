package org.espenhahn.util.pool;

import java.util.Comparator;
import java.util.TreeSet;

public class SortedPool<T extends Sizable> {

	private final int MAX_BUFFERS = 20;
	private final Comparator<Sizable> COMPARATOR = new Comparator<Sizable>() {

		@Override
		public int compare(Sizable arg0, Sizable arg1) {
			return arg0.getSize()- arg1.getSize();
		}
		
	};
	
	private TreeSet<Sizable> pool;
	private ObjectInstantiator<T> instantiator;
	private int avaliable;
	
	public SortedPool(ObjectInstantiator<T> instantiator) {
		this.pool = new TreeSet<Sizable>(COMPARATOR);
		this.instantiator = instantiator;
		this.avaliable = MAX_BUFFERS;
	}

	/**
	 * Take an object from the pool
	 * @return An object that isn't in use
	 */
	@SuppressWarnings("unchecked")
	public synchronized T take(int minSize) {
		T obj = (T) pool.higher(new Sizable() {

			@Override
			public int getSize() {
				return minSize;
			}
			
		});
		
		while (avaliable < 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		avaliable -= 1;
		
		if (obj != null) 
			return obj;
		else 
			return instantiator.newInstance(minSize*2);
	}
	
	/**
	 * Give the object back to the pool
	 * @param obj The object
	 * @return True if the pool took it, false if the pool is full
	 */
	public synchronized boolean put(T obj) {
		avaliable += 1;
	
		try {
			if (pool.size() >= MAX_BUFFERS) {
				Sizable small = this.pool.first();
				if (small.getSize() < obj.getSize()) {
					pool.remove(small);
					pool.add(obj);
					return true;
				}
			} else {
				pool.add(obj);
				return true;
			}
			
			return false;
		} finally {
			notify();
		}
	}
	
}
