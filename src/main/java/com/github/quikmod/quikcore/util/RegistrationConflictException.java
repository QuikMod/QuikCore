/*
 */
package com.github.quikmod.quikcore.util;

/**
 *
 * @author Ryan
 */
public class RegistrationConflictException extends RuntimeException {
	
	public static final String FORMAT = "Duplicate registration in '%1$s'\n\t- Location: '%2$s'\n\t- Existing: '%3$s'\n\t- New: '%4$s'\n";
	
	public RegistrationConflictException(Object from, Object site, Object old, Object conflicting) {
		super("\n" + String.format(FORMAT, from, site, old, conflicting));
	}

	public RegistrationConflictException(Object from, Object at, Object old, Object conflicting, String message) {
		super(message + "\n" + String.format(FORMAT, from, at, old, conflicting));
	}
	
}
