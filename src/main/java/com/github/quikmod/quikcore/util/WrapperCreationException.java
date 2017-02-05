/*
 */
package com.github.quikmod.quikcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
		super(String.format(FORMAT, "Method Wrapper", method.getName(), method.getDeclaringClass().getName(), reason));
	}
	
	/**
	 * Constructs an instance of <code>WrapperCreationException</code> with
	 * the specified detail message.
	 *
	 * @param method the method that was being wrapped.
	 * @param reason the reason for the creation error.
	 * @param cause the exception that caused the exception.
	 */
	public WrapperCreationException(Method method, String reason, Exception cause) {
		super(String.format(FORMAT, "Method Wrapper", method.getName(), method.getDeclaringClass().getName(), reason), cause);
	}
	
	/**
	 * Constructs an instance of <code>WrapperCreationException</code> with
	 * the specified detail message.
	 *
	 * @param method the method that was being wrapped.
	 * @param param the parameter that was being wrapped.
	 * @param reason the reason for the creation error.
	 */
	public WrapperCreationException(Method method, Parameter param, String reason) {
		super(String.format("Method Wrapper: %2$s.%1$s parameter %3$s %4$s", method.getName(), method.getDeclaringClass().getName(), param, reason));
	}
	
	/**
	 * Constructs an instance of <code>WrapperCreationException</code> with
	 * the specified detail message.
	 *
	 * @param method the method that was being wrapped.
	 * @param param the parameter that was being wrapped.
	 * @param reason the reason for the creation error.
	 * @param cause the exception that caused the exception.
	 */
	public WrapperCreationException(Method method, Parameter param, String reason, Exception cause) {
		super(String.format("Method Wrapper: %2$s.%1$s parameter %3$s %4$s", method.getName(), method.getDeclaringClass().getName(), param, reason), cause);
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
	
	/**
	 * Constructs an instance of <code>WrapperCreationException</code> with
	 * the specified detail message.
	 *
	 * @param field the field that was being wrapped.
	 * @param reason the reason for the creation error.
	 * @param cause the exception that caused this exception.
	 */
	public WrapperCreationException(Field field, String reason, Exception cause) {
		super(String.format(FORMAT, "Field Wrapper", field.getName(), field.getType().getCanonicalName(), reason), cause);
	}

}
