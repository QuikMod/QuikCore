/*
 */
package com.github.quikmod.quikcore.network;

/**
 *
 * @author Ryan
 */
public interface QuikMessage {
	
	public void fromBytes(QuikByteBuffer buffer);
	
	public void toBytes(QuikByteBuffer buffer);
	
	public void onMessage();
	
}
