/*
 */
package com.github.quikmod.quikcore.reflection.exceptions;

/**
 *
 * @author Ryan
 */
public class UnknownQuikDomainException extends Exception {

	/**
	 * Constructs an instance of <code>UnknownQuikDomainException</code> with
	 * the specified detail message.
	 *
	 * @param path the path of unknown domain.
	 */
	public UnknownQuikDomainException(String path) {
		super("'" + path + "' has no known QuikDomain!");
	}
}
