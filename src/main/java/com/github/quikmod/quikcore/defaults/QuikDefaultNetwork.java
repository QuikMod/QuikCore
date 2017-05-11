/*
 */
package com.github.quikmod.quikcore.defaults;

import com.github.quikmod.quikcore.network.QuikMessage;
import com.github.quikmod.quikcore.network.QuikMessageHandler;
import com.github.quikmod.quikcore.network.QuikNetworkAdaptor;

/**
 *
 * @author Ryan
 */
public class QuikDefaultNetwork implements QuikNetworkAdaptor {

	@Override
	public void sendMessage(QuikMessage message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void registerMessageHandler(QuikMessageHandler handler) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
