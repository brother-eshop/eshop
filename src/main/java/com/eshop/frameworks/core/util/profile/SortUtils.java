package com.eshop.frameworks.core.util.profile;

import java.util.Comparator;

public class SortUtils {
	private SortUtils() {}
	
	public static <T extends Comparable<T>> Comparator<T> comp(Class<T> clazz) {
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return o1.compareTo(o2);
			}
		};
	}
	
	public static <T extends Comparable<T>> Comparator<T> reverse(Class<T> clazz) {
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return -o1.compareTo(o2);
			}
		};
	}
	
	public static class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>> {
		private K key;
		private V value;
		
		public Pair(K k, V v) {
			this.key = k;
			this.value = v;
		}
		
		@Override
		public int compareTo(Pair<K, V> p) {
			int r = key.compareTo(p.key);
			if (r == 0) r = value.compareTo(p.value);
			return r;
		}
		
		public static <K extends Comparable<K>, V extends Comparable<V>> Pair<K, V> make(K k, V v) {
			return new Pair<K, V>(k, v);
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		@Override
		public String toString() {
			return key + "," + value;
		}
	}
}