/*
 */
package com.github.quikmod.quikcore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author RlonRyan
 */
public class TypeSupplierMap {
	
	private final Map<Class, Supplier> suppliers;

	public TypeSupplierMap() {
		this.suppliers = new HashMap<>();
	}
	
	public <T> Supplier<T> put(Class<T> clazz, Supplier<T> supplier) {
		return this.suppliers.put(clazz, supplier);
	}
	
	public <T> Supplier<T> putIfAbsent(Class<T> clazz, Supplier<T> supplier) {
		return this.suppliers.putIfAbsent(clazz, supplier);
	}
	
	public <T> Supplier<T> get(Class<T> clazz) {
		return this.suppliers.get(clazz);
	}
	
	public <T> Supplier<T> getOrDefault(Class<T> clazz, Supplier<T> defaultSupplier) {
		return this.suppliers.getOrDefault(clazz, defaultSupplier);
	}
	
	public <T> Supplier<T> replace(Class<T> clazz, Supplier<T> supplier) {
		return this.suppliers.replace(clazz, supplier);
	}
	
	public <T> Supplier<T> remove(Class<T> clazz) {
		return this.suppliers.remove(clazz);
	}
	
	public <T> boolean containsKey(Class<T> clazz) {
		return this.suppliers.containsKey(clazz);
	}
	
	public <T> boolean containsValue(Supplier<T> consumer) {
		return this.suppliers.containsValue(consumer);
	}
	
}
