/*
 */
package com.github.quikmod.quiklib.command;

import com.github.quikmod.quiklib.core.QuikCore;
import com.github.quikmod.quiklib.util.WrapperCreationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Ryan
 */
public class QuikCommandWrapper {

	private final Method method;
	private final String name;
	private final String info;
	private final String perm;
	private final List<QuikParamWrapper> parameters;

	public static final Optional<QuikCommandWrapper> attemptWrapperCreation(Method method) {
		try {
			return Optional.of(new QuikCommandWrapper(method));
		} catch (WrapperCreationException e) {
			QuikCore.getCoreLogger().trace(e);
			return Optional.empty();
		}
	}

	public QuikCommandWrapper(Method method) throws WrapperCreationException {
		// Save method reference for invocation.
		this.method = method;

		// Fetch Command Annotation
		QuikCommand q = method.getAnnotation(QuikCommand.class);
		if (q == null) {
			throw new WrapperCreationException(method, "A quick command method must be annotated with @QuikCommand!");
		}

		// Handle Command Infomation
		this.name = q.name().trim().toLowerCase();
		this.info = q.info().trim();
		this.perm = q.perm().trim();

		// Handle Parameters
		this.parameters = new ArrayList<>(method.getParameterCount());
		for (Parameter p : method.getParameters()) {
			this.parameters.add(new QuikParamWrapper(method, p));
		}
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}

	public String getPerm() {
		return perm;
	}

	public QuikInvocationResult invoke(Map<String, String> paramMap) {
		Object[] args = new Object[this.parameters.size()];
		if (args.length > 1) {
			for (int i = 0; i < this.parameters.size(); i++) {
				QuikParamWrapper pw = this.parameters.get(i);
				QuikParamConversionResult res = pw.handleConversion(this, paramMap.get(pw.getTag()));
				if (res.type != QuikResultType.OK) {
					return QuikInvocationResult.fromParameterResult(this, pw, res);
				}
				args[i] = res.result;
			}
		} else if (args.length == 1) {
			QuikParamWrapper pw = this.parameters.get(0);
			QuikParamConversionResult res = pw.handleConversion(this, paramMap.getOrDefault(pw.getTag(), paramMap.get("")));
			if (res.type != QuikResultType.OK) {
				return QuikInvocationResult.fromParameterResult(this, pw, res);
			}
			args[0] = res.result;
		}
		try {
			return QuikInvocationResult.fromSuccess(this, this.method.invoke(null, args));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			QuikCore.getCoreLogger().debug("Arguments: {0}", Arrays.toString(args));
			QuikCore.getCoreLogger().debug("Param Types: {0}", Arrays.toString(this.method.getParameterTypes()));
			return QuikInvocationResult.fromCommandException(this, e);
		}
	}

	@Override
	public String toString() {
		return String.format("QuikCommandWrapper { method: \"%s\", name: \"%s\", info: \"%s\", perm: \"%s\", parameters: \"%s\" }", method, name, info, perm, parameters);
	}

}
