/*
 */
package com.github.quikmod.quikcore.config;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 *
 * @author Ryan
 */
public class QuikConfigurableWrapper {

	private final Object container;
	private final Field configurable;
	private final Object defaultValue;
	private final double minValue;
	private final double maxValue;
	private final boolean isNumber;

	public QuikConfigurableWrapper(Object container, Field configurable) throws WrapperCreationException {
		final QuikConfigurable anno = configurable.getAnnotation(QuikConfigurable.class);
		if (anno == null) {
			throw new WrapperCreationException(configurable, "Field is not a @QuikConfigurable!");
		}
		this.container = container;
		this.configurable = configurable;
		try {
			this.defaultValue = this.configurable.get(container);
		} catch(IllegalAccessException | IllegalArgumentException e) {
			throw new WrapperCreationException(configurable, "Error fetching default value!");
		}
		this.minValue = anno.min();
		this.maxValue = anno.max();
		this.isNumber = Number.class.isAssignableFrom(configurable.getClass());
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public boolean isNumber() {
		return isNumber;
	}

	public boolean set(Object value) {
		if (this.configurable.getType().isAssignableFrom(value.getClass())) {
			try {
				this.configurable.set(container, value);
				return true;
			} catch (IllegalAccessException | IllegalArgumentException e) {
				QuikCore.getCoreLogger().trace(e);
			}
		}
		return false;
	}

	public Optional<?> get() {
		try {
			return Optional.ofNullable(this.configurable.get(container));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			return Optional.empty();
		}
	}

}
