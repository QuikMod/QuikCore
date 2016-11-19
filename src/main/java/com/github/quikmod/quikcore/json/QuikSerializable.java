/*
 */
package com.github.quikmod.quikcore.json;

/**
 * Marker interface for classes that can be serialized by AgriCore. Classes that
 * are annotated with this annotation must have a string "path" field.
 *
 * @author RlonRyan
 */
public interface QuikSerializable {
	
	boolean isEnabled();
	
	String getPath();
	
	void setEnabled(boolean enabled);
	
	void setPath(String path);

}
