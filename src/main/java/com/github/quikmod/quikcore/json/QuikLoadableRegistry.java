/*
 */
package com.github.quikmod.quikcore.json;

/**
 *
 * @author RlonRyan
 */
public interface QuikLoadableRegistry<T> {
	
	boolean acceptsElement(String filename);
	
	Class<T> getElementClass();
	
	void registerElement(T element);
	
	void clearElements();
	
}
