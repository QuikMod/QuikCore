/*
 */
package com.github.quikmod.quikcore.network;

/**
 *
 * @author Ryan
 */
@FunctionalInterface
public interface QuikMessageHandler {
	
	public void onMessageRecieved(QuikByteBuffer buffer);
	
}
