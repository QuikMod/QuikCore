/*
 */
package com.github.quikmod.quikcore.injection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.reflection.exceptions.UnknownQuikDomainException;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * @author Ryan
 */
public class QuikInjectedWrapper {

	private final Object container;
	private final Field site;
	private final String domain;
	private final boolean optional;
	private final boolean nullable;

	public QuikInjectedWrapper(Object container, Field site) throws WrapperCreationException {
		// Ensure Field Not Null
		if (site == null) {
			throw new WrapperCreationException(site, "A @QuikInjected field cannot be null!");
		}

		// Ensure Proper Annotation
		if (!site.isAnnotationPresent(QuikInjected.class)) {
			throw new WrapperCreationException(site, "A @QuikInjected must be annotated with @QuikInjected!");
		}

		// Ensure Proper Container
		if (container == null) {
			if (!Modifier.isStatic(site.getModifiers())) {
				throw new WrapperCreationException(site, "A @QuikInjected must be a static field if no instance is registered!");
			}
		} else {
			if (!site.getDeclaringClass().isAssignableFrom(container.getClass())) {
				throw new WrapperCreationException(site, "Invalid container '" + container.getClass().getName() + "'!");
			}
		}

		// Ensure Not Final
		if (Modifier.isFinal(site.getModifiers())) {
			throw new WrapperCreationException(site, "A @QuikInjected field must not be final!");
		}

		// Determine Domain
		try {
			this.domain = QuikCore.getDomains().resolveDomain(site);
		} catch (UnknownQuikDomainException e) {
			throw new WrapperCreationException(site, "A @QuikInjected must have a domain!", e);
		}

		// Ensure Accessable
		try {
			site.setAccessible(true);
		} catch (SecurityException e) {
			throw new WrapperCreationException(site, "A @QuikInjected must be accessible!", e);
		}

		// Fetch Annotation.
		QuikInjected anno = site.getAnnotation(QuikInjected.class);

		this.site = site;
		this.container = container;
		this.optional = anno.optional();
		this.nullable = anno.nullable();
	}

	public Class<?> getType() {
		return site.getType();
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return site.getDeclaringClass().getName() + "." + site.getName();
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isOptional() {
		return optional;
	}

	public void set(Object obj) throws IllegalArgumentException, NullPointerException {
		if (obj == null && !this.nullable) {
			throw new NullPointerException("Attempted to inject null into non-nullable @QuikInjected field: '" + site.toGenericString() + "'!");
		}
		try {
			this.site.set(container, obj);
		} catch (IllegalAccessException ax) {
			throw new Error("@QuikInjected site: '" + this.site.getName() + "' cannot be accessed! This should be impossible!", ax);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof QuikInjectedWrapper) && ((QuikInjectedWrapper) obj).site.equals(this.site);
	}

	@Override
	public int hashCode() {
		return this.site.hashCode();
	}

	@Override
	public String toString() {
		return String.format(
				"QuikInjected{ Field: \"%2$s.%1$s\", Type: \"%3$s\", Domain: \"%4$s\" }",
				site.getName(),
				site.getDeclaringClass().getName(),
				site.getType(),
				domain
		);
	}

}
