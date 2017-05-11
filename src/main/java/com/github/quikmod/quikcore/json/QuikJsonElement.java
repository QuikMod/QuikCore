/*
 */
package com.github.quikmod.quikcore.json;

/**
 * Marker interface for classes that can be serialized by QuikCore.
 *
 * @author RlonRyan
 */
public interface QuikJsonElement {
	
	String getId();

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);

}
