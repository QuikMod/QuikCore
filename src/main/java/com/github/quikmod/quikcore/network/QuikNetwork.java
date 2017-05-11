/*
 */
package com.github.quikmod.quikcore.network;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * QuikCore network interface manager.
 *
 * @author Ryan
 */
public final class QuikNetwork {

	private final QuikNetworkAdaptor adaptor;

	private final List<Class<QuikMessage>> messages;

	public QuikNetwork(QuikNetworkAdaptor adaptor) {
		this.adaptor = adaptor;
		this.messages = new ArrayList<>();
	}

	/**
	 * Registers a message class to the internal handler.
	 *
	 * @param clazz The message class to be registered.
	 */
	public void registerMessage(Class<?> clazz) {
		if (QuikMessage.class.isAssignableFrom(clazz)) {
			messages.add((Class<QuikMessage>) clazz);
		}
	}

	/**
	 * Called when the network interface receives a QuikMessage. This method
	 * handles the conversion from the byte representation of a message back
	 * into the object form of the message.
	 *
	 * @param buffer The byte buffer containing the byte representation of the
	 * message.
	 */
	public void onMessageRecieved(QuikByteBuffer buffer) {
		final int id = buffer.getInteger();
		if (id >= messages.size()) {
			QuikCore.getCoreLogger().error("Network: There is no mapping for message id: {0}!", id);
			return;
		}
		ReflectionHelper
				.attemptInstantiate(messages.get(id))
				.ifPresent(m -> {
					m.fromBytes(buffer);
					m.onMessage();
				});
	}

	/**
	 * Sends a message using the wrapped network interface.
	 *
	 * @param message The message to be sent.
	 */
	public void sendMessage(QuikMessage message) {
		this.adaptor.sendMessage(message);
	}

}
