/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.conversion;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author RlonRyan
 */
public final class QuikConverterManager {

	private static final ConcurrentHashMap<Class<?>, QuikConverterWrapper> CONVERTERS = new ConcurrentHashMap<>();

    private QuikConverterManager() {
        // Nothing to see here...
    }

	public static boolean hasConverterFor(Class<?> type) {
		return CONVERTERS.containsKey(type);
	}

	public static <T> Optional<T> convert(Class<T> type, String value) {
		final QuikConverterWrapper converter = CONVERTERS.get(type);
		if (converter != null) {
			return converter.invoke(type, value);
		} else {
			QuikCore.getCoreLogger().debug("Missing converter for: {0}", type);
			return Optional.empty();
		}
	}

	public static void addConverters(Class converterClass) {
		for (Method m : converterClass.getMethods()) {
			if (m.isAnnotationPresent(QuikConverter.class)) {
				addConverter(m);
			}
		}
	}

	public static void addConverter(Method m) {
		try {
			final QuikConverterWrapper wrapper = new QuikConverterWrapper(m);
			CONVERTERS.putIfAbsent(wrapper.getReturnType(), wrapper);
			QuikCore.getCoreLogger().debug("Registered converter method {0} for type {1}.", m.getName(), m.getReturnType().getName());
		} catch (WrapperCreationException e) {
			QuikCore.getCoreLogger().trace(e);
		}
	}

}
