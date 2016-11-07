/*
 */
package com.github.quikmod.quiklib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Ryan
 */
public class WrapperCreationException extends Exception {

	public static final String FORMAT = "%1$s: %3$s.%2$s %4$s";

	/**
	 * Constructs an instance of <code>WrapperCreationException</code> with
	 * the specified detail message.
	 *
	 * @param method the method that was being wrapped.
	 * @param reason the reason for the creation error.
	 */
	public WrapperCreationException(Method method, String reason) {
		super(String.format(FORMAT, "Method Wrapper", method.getName(), method.getClass().getCanonicalName(), reason));
	}

	/**
	 * Constructs an instance of <code>WrapperCreationException</code> with
	 * the specified detail message.
	 *
	 * @param field the field that was being wrapped.
	 * @param reason the reason for the creation error.
	 */
	public WrapperCreationException(Field field, String reason) {
		super(String.format(FORMAT, "Field Wrapper", field.getName(), field.getType().getCanonicalName(), reason));
	}

}
