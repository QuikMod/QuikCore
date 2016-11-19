/*
 */
package com.github.quikmod.quikcore.command;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.TypeHelper;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 *
 * @author Ryan
 */
public class QuikParamWrapper {

	private final Class type;
	
	private final String tag;
	private final String info;
	private final String or;
	private final boolean optional;
	private final boolean nullable;

	public QuikParamWrapper(Method method, Parameter parameter) throws WrapperCreationException {
		// Save parameter type class.
		this.type = TypeHelper.advance(parameter.getType());
		
		// Fetch Parameter Annotation
		QuikParam anno = parameter.getAnnotation(QuikParam.class);
		if (anno == null) {
			throw new WrapperCreationException(method, parameter, "A quik command method parameter must be annotated with @QuikParam!");
		}
		
		// Check that type is valid.
		if (!QuikCore.getConverters().hasConverterFor(type)) {
			throw new WrapperCreationException(method, parameter, "There exists no @QuikConverter capable of handling the given command argument of type '" + type + "'!");
		}
		
		// Wrap parameter information
		this.tag = anno.tag().trim();
		this.info = anno.info().trim();
		this.or = anno.or().trim();
		this.optional = anno.optional();
		this.nullable = anno.nullable();
	}

	public Class getType() {
		return type;
	}

	public String getTag() {
		return tag;
	}

	public String getInfo() {
		return info;
	}

	public String getDefault() {
		return or;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isNullable() {
		return nullable;
	}

	public QuikParamConversionResult handleConversion(QuikCommandWrapper command, String value) {
		if (value == null && this.isOptional()) {
			value = this.getDefault();
		}
		if (value == null) {
			return new QuikParamConversionResult(QuikResultType.MISSING_PARAM, this, value, this.type.isPrimitive() ? 0 : null);
		}
		Object actual = QuikCore.getConverters().convert(this.type, value).orElse(null);
		if (actual == null && !this.isNullable()) {
			return new QuikParamConversionResult(QuikResultType.BAD_PARAM, this, value, this.type.isPrimitive() ? 0 : null);
		}
		return new QuikParamConversionResult(QuikResultType.OK, this, value, actual);
	}

	@Override
	public String toString() {
		return String.format("QuikParamWrapper { tag: \"%s\", info: \"%s\", or: \"%s\", optional: \"%s\", nullable: \"%s\", type: \"%s\" }", tag, info, or, optional, nullable, type);
	}

}
