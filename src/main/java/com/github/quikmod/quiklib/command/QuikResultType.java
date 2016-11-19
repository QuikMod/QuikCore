/*
 */
package com.github.quikmod.quiklib.command;

/**
 *
 * @author Ryan
 */
public enum QuikResultType {
	
	OK("Ok"),
	BAD_PARAM("Bad Parameter"),
	BAD_COMMAND("Bad Parameter"),
	MISSING_PARAM("Missing Parameter"),
	MISSING_COMMAND("Missing Command"),
	EXCEPTION("Exception");
	
	public final String formatted;

	private QuikResultType(String formatted) {
		this.formatted = formatted;
	}
	
}
