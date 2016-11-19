/*
 */
package com.github.quikmod.quiklib.command;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ryan
 */
public class QuikParamConversionResult {

	final QuikResultType type;
	final QuikParamWrapper parameter;
	final Object result;
	final String input;

	public QuikParamConversionResult(QuikResultType type, QuikParamWrapper parameter, String input, Object result) {
		this.result = result;
		this.parameter = parameter;
		this.type = type;
		this.input = input;
	}

	public QuikResultType getType() {
		return type;
	}

	public QuikParamWrapper getParameter() {
		return parameter;
	}

	public Object getResult() {
		return result;
	}

	public String getInput() {
		return input;
	}

	public List<String> getLines() {
		return Arrays.asList(
				"Parameter: '" + this.parameter.getTag() + "'",
				"Type: '" + this.parameter.getType() + "'",
				"Value: '" + this.input + "'",
				"Error: " + this.type.formatted + " Parameter!"
		);
	}

	@Override
	public String toString() {
		return String.format("QuikParamConversionResult { type: \"%s\", parameter: \"%s\", input: \"%s\", result: \"%s\" } ", type, parameter, result, input);
	}

}
