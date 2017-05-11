/*
 */
package com.github.quikmod.quikcore.network;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 * @author Ryan
 */
public class JavaByteBufferWrapper implements QuikByteBuffer {

	private final ByteBuffer buffer;
	
	public JavaByteBufferWrapper(ByteBuffer buffer) {
		this.buffer = Objects.requireNonNull(buffer);
	}

	@Override
	public int getSize() {
		return this.buffer.capacity();
	}

	@Override
	public int getFree() {
		return this.buffer.remaining();
	}

	@Override
	public byte getByte() {
		return this.buffer.get();
	}

	@Override
	public short getShort() {
		return this.buffer.getShort();
	}

	@Override
	public int getInteger() {
		return this.buffer.getInt();
	}

	@Override
	public long getLong() {
		return this.buffer.getLong();
	}

	@Override
	public float getFloat() {
		return this.buffer.getFloat();
	}

	@Override
	public double getDouble() {
		return this.buffer.getDouble();
	}

	@Override
	public String getString() {
		final int length = this.buffer.getInt();
		final byte[] bytes = new byte[length];
		this.buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	@Override
	public void getBytes(byte[] dest, int count) {
		this.buffer.get(dest, 0, count);
	}

	@Override
	public void addByte(byte b) {
		this.buffer.put(b);
	}

	@Override
	public void addShort(short s) {
		this.buffer.putShort(s);
	}

	@Override
	public void addInteger(int i) {
		this.buffer.putInt(i);
	}

	@Override
	public void addLong(int l) {
		this.buffer.putLong(l);
	}

	@Override
	public void addFloat(float f) {
		this.buffer.putFloat(f);
	}

	@Override
	public void addDouble(double d) {
		this.buffer.putDouble(d);
	}

	@Override
	public void addString(String s) {
		this.buffer.putInt(s.length());
		this.buffer.put(s.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void flip() {
		this.buffer.flip();
	}
	
}
