/*
 */
package com.github.quikmod.quikcore.command;

import com.github.quikmod.quikcore.core.QuikCore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Ryan
 */
public class QuikInvocationResult {
	
	final QuikResultType type;
	final List<String> output;
	
	private QuikInvocationResult(QuikResultType result, List<String> output) {
		this.type = result;
		this.output = output;
	}
	
	public static final QuikInvocationResult fromNothing() {
		return new QuikInvocationResult(
				QuikResultType.MISSING_COMMAND,
				Arrays.asList(
						"You can't invoke nothing!"
				)
		);
	}
	
	public static final QuikInvocationResult fromMissing(String input) {
		return new QuikInvocationResult(
				QuikResultType.MISSING_COMMAND,
				Arrays.asList(
						"There is no command that accepts '" + input + "'!"
				)
		);
	}
	
	public static final QuikInvocationResult fromAmbiguous(String input, String... possibilities) {
		List<String> lines = new ArrayList<>(1 + possibilities.length);
		lines.add("The command input '" + input + "' is ambiguous!");
		if (possibilities.length > 0) {
			lines.add("Did you mean: ");
			for (String p : possibilities) {
				lines.add("\t- '" + p + "'");
			}
		}
		return new QuikInvocationResult(QuikResultType.MISSING_COMMAND, lines);
	}
	
	public static final QuikInvocationResult fromSuccess(QuikCommandWrapper command, Object result) {
		return new QuikInvocationResult(
				QuikResultType.OK,
				Arrays.asList(
						String.valueOf(result)
				)
		);
	}
	
	public static final QuikInvocationResult fromParameterResult(QuikCommandWrapper command, QuikParamWrapper parameter, QuikParamConversionResult result) {
		List<String> lines = result.getLines().stream().map(e -> "\t- " + e).collect(Collectors.toList());
		lines.add(0, "Command: '" + command.getName() + "'");
		return new QuikInvocationResult(result.getType(), lines);
	}
	
	public static final QuikInvocationResult fromCommandException(QuikCommandWrapper command, Exception e) {
		QuikCore.getCoreLogger().trace(e);
		return new QuikInvocationResult(
				QuikResultType.EXCEPTION,
				Arrays.asList(
						"Command: '" + command.getName() + "'",
						"\tException: '" + e.getLocalizedMessage() + "'",
						"\tNotice:",
						"\t\tThis is not QuikCore's fault!",
						"\t\tContact the command author for help!",
						"\t\tThe exception has been written to the server log."
				)
		);
	}
	
	public List<String> getOutput() {
		return output;
	}
	
	@Override
	public String toString() {
		return String.format("QuikInvocationResult { type: \"%s\", output: %s }", type, output);
	}
	
}
