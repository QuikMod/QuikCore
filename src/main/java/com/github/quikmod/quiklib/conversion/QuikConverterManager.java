/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quiklib.conversion;

import com.github.quikmod.quiklib.core.QuikCore;
import com.github.quikmod.quiklib.util.WrapperCreationException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author RlonRyan
 */
public final class QuikConverterManager {

	private final Map<Class<?>, QuikConverterWrapper> converters;

	public QuikConverterManager() {
		converters = new HashMap<>();
	}

	public boolean hasConverterFor(Class<?> type) {
		return converters.containsKey(type);
	}

	public <T> Optional<T> convert(Class<T> type, String value) {
		final QuikConverterWrapper converter = converters.get(type);
		if (converter != null) {
			return converter.invoke(type, value);
		} else {
			return Optional.empty();
		}
	}

	public void addConverters(Class converterClass) {
		for (Method m : converterClass.getMethods()) {
			if (m.isAnnotationPresent(QuikConverter.class)) {
				addConverter(m);
			}
		}
	}

	public void addConverter(Method m) {
		try {
			final QuikConverterWrapper wrapper = new QuikConverterWrapper(m);
			converters.putIfAbsent(wrapper.getReturnType(), wrapper);
		} catch (WrapperCreationException e) {
			QuikCore.getCoreLogger().trace(e);
		}
	}

}
