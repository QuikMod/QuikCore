/*
 */
package com.github.quikmod.quikcore.util;

import java.util.AbstractMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class MathHelper {
	
	/**
	 * Brings an integer into a specified range.
	 *
	 * @param value The value to bring into the range.
	 * @param min The minimum value, inclusive.
	 * @param max The maximum value, inclusive.
	 * @return The in-bounded value.
	 */
	public static int inRange(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	/**
	 * Brings an integer into a specified range.
	 *
	 * @param value The value to bring into the range.
	 * @param min The minimum value, inclusive.
	 * @param max The maximum value, inclusive.
	 * @return The in-bounded value.
	 */
	public static float inRange(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
	/**
	 * Brings an integer into a specified range.
	 *
	 * @param value The value to bring into the range.
	 * @param min The minimum value, inclusive.
	 * @param max The maximum value, inclusive.
	 * @return The in-bounded value.
	 */
	public static double inRange(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}
	
	/**
	 * Creates a standard map entry (ie a pair) from the given key and value.
	 * 
	 * @param <K> The type of the key, automatically inferred.
	 * @param <V> The type of the value, automatically inferred.
	 * @param key The key.
	 * @param value The value.
	 * @return A map entry of the given key and value.
	 */
	public static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
		return new AbstractMap.SimpleEntry<>(key, value);
	}
	
}
