/*
 */
package com.github.quikmod.quikcore.network;

/**
 *
 * @author Ryan
 */
public interface QuikByteBuffer {

	int getSize();

	int getFree();

	byte getByte();

	short getShort();

	int getInteger();

	long getLong();

	float getFloat();

	double getDouble();

	String getString();

	void getBytes(byte[] dest, int count);

	void addByte(byte b);

	void addShort(short s);

	void addInteger(int i);

	void addLong(int l);

	void addFloat(float f);

	void addDouble(double d);

	void addString(String s);
	
	void flip();

}
