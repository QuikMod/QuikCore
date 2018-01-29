/*
 */
package com.github.quikmod.quikcore.injection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.TypeHelper;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author Ryan
 */
public class QuikInjectorWrapper {

	private MethodHandle provider;
	private final Class<?> type;
	private final String domain;
	private final String name;

	public QuikInjectorWrapper(Object container, Method injector) throws WrapperCreationException {
		// Ensure that function is injector.
		if (!injector.isAnnotationPresent(QuikInjector.class)) {
			throw new WrapperCreationException(injector, "A @QuikInjector must be annotated with @QuikInjector!");
		}

		// Ensure that the function accepts proper number of arguments.
		if (injector.getParameterCount() != 1) {
			throw new WrapperCreationException(injector, "A @QuikInjector must accept a single argument! This one accepts " + injector.getParameterCount() + "!");
		}

		// Ensure that arguments are of proper type.
		if (!String.class.isAssignableFrom(injector.getParameters()[0].getType())) {
			throw new WrapperCreationException(injector, injector.getParameters()[0], "A QuikInjector must accept a domain string as its only argument!");
		}

		// Make accessable.
		if (Modifier.isStatic(injector.getModifiers())) {
			container = null;
		} else if (container == null) {
			throw new WrapperCreationException(injector, "A @QuikInjector must be a static method if no instance is given!");
		} else if (!injector.getDeclaringClass().isAssignableFrom(container.getClass())) {
			throw new WrapperCreationException(injector, "Invalid container '" + container.getClass().getName() + "'!");
		}

		// Fetch the domain. "" means valid for all domains.
		this.domain = QuikCore.getDomains().attemptResolveDomain(injector).orElse("");

		// Set the name.
		this.name = container + "#" + injector.getDeclaringClass().getName() + "." + injector.getName();

		// Fetch the return type.
		this.type = injector.getReturnType();

		// Ensure that injector actuall returns something.
		if (TypeHelper.advance(this.type).equals(Void.class)) {
			throw new WrapperCreationException(injector, "A QuikInject must return a non-void value!");
		}

		// Fetch the handle.
		try {
			if (container == null) {
				this.provider = MethodHandles.lookup().unreflect(injector);
			} else {
				this.provider = MethodHandles.lookup().unreflect(injector).bindTo(container);
			}
		} catch (IllegalAccessException e) {
			throw new WrapperCreationException(injector, "A QuikInjector must be accessible!");
		}
	}

	public Class<?> getType() {
		return type;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public void inject(QuikInjectedWrapper target) {
		// The object to inject.
		final Object obj;

		// Get the object to inject.
		try {
			obj = this.provider.invoke(target.getDomain());
		} catch (Throwable t) {
			throw new RuntimeException("Error applying injector '" + this.name + "' in domain '" + target.getDomain() + "'!", t);
		}

		// Ensure the injected object is compatible.
		if (obj == null && !target.isNullable()) {
			if (!target.isOptional()) {
				throw new NullPointerException("Injector '" + this.name + "' attempted to inject null into non-nullable " + target + "!");
			} else {
				QuikCore.getCoreLogger().warn("Injector \"{0}\" attempted to inject null into non-nullable {1}! Skipping optional injection as a result!", this.name, target);
				return;
			}
		}

		// Perform Injection.
		target.set(obj);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof QuikInjectorWrapper) 
				&& this.name.equals(((QuikInjectorWrapper) obj).name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
