/*
 */
package com.github.quikmod.quiklib.json;

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
