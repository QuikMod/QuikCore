/*
 */
package com.github.quikmod.quikcore.network;

/**
 *
 * @author Ryan
 */
public interface QuikNetworkAdaptor {
	
	void registerMessageHandler(QuikMessageHandler handler);
	
	void sendMessage(QuikMessage message);
	
}
