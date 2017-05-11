/*
 */
package com.github.quikmod.quikcore.network;

/**
 *
 * @author Ryan
 */
public class StringMessage implements QuikMessage {
	
	private String message;

	public StringMessage() {
	}

	public StringMessage(String message) {
		this.message = message;
	}

	@Override
	public void fromBytes(QuikByteBuffer buffer) {
		this.message = buffer.getString();
	}

	@Override
	public void toBytes(QuikByteBuffer buffer) {
		buffer.addString(message);
	}

	@Override
	public void onMessage() {
		System.out.println("Recieved Message: \"" + message + "\"");
	}
	
}
