/*
 */
package com.github.quikmod.quiklib.conversion;

import com.github.quikmod.quiklib.core.QuikCore;
import com.github.quikmod.quiklib.util.WrapperCreationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

/**
 *
 * @author Ryan
 */
public class QuikConverterWrapper {

	private final Method converter;
	private final Class<?> returnType;

	public QuikConverterWrapper(Method converter) throws WrapperCreationException {
		if (converter.getAnnotation(QuikConverter.class) == null) {
			throw new WrapperCreationException(converter, "is not a @QuikConverter!");
		} else if (!(Modifier.isStatic(converter.getModifiers()) && Modifier.isPublic(converter.getModifiers()))) {
			throw new WrapperCreationException(converter, "is not public static!");
		} else if (converter.getParameterTypes().length != 1) {
			throw new WrapperCreationException(converter, "does not accept proper number of arguments!");
		} else if (converter.getParameterTypes()[0] != String.class) {
			throw new WrapperCreationException(converter, "does not accept proper type for conversion input!");
		}
		this.converter = converter;
		this.returnType = converter.getReturnType();
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public <T> Optional<T> invoke(Class<T> toClass, String value) {
		if (toClass.isAssignableFrom(returnType)) {
			try {
				return Optional.ofNullable(toClass.cast(converter.invoke(null, value)));
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
				QuikCore.getCoreLogger().trace(e);
			}
		}
		return Optional.empty();
	}

}
