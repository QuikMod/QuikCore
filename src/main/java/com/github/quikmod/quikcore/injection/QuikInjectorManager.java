/*
 */
package com.github.quikmod.quikcore.injection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.ReflectionStreams;
import com.github.quikmod.quikcore.util.RegistrationConflictException;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author Ryan
 */
public class QuikInjectorManager {

	private final Map<String, Set<QuikInjectedWrapper>> targets;
	private final Map<String, Map<Class, QuikInjectorWrapper>> injectors;

	private boolean used;

	public QuikInjectorManager() {
		this.targets = new HashMap<>();
		this.injectors = new HashMap<>();
		this.injectors.put("", new HashMap<>());
		this.used = false;
	}

	public synchronized void registerInjectable(Object container, Field site) throws WrapperCreationException {
		// Create Injectable Wrapper.
		final QuikInjectedWrapper target = new QuikInjectedWrapper(container, site);
		// Ensure Domain Set Exists.
		if (!this.targets.containsKey(target.getDomain())) {
			targets.put(target.getDomain(), new HashSet<>());
		}
		// Insert Injectable.
		this.targets.get(target.getDomain()).add(target);
		// Notify
		QuikCore.getCoreLogger().info("Registered Injection Site: {0}.", target.getName());
		// Update Injection.
		if (used) {
			performInjection(target);
		}
	}

	public synchronized void registerInjector(Object container, Method m) throws WrapperCreationException {
		// Create Wrapper
		final QuikInjectorWrapper injector = new QuikInjectorWrapper(container, m);
		final String domain = injector.getDomain();
		// Ensure Domain Map Exists.
		if (!this.injectors.containsKey(domain)) {
			this.injectors.put(domain, new HashMap());
		}
		// Add the wrapper.
		final QuikInjectorWrapper result = this.injectors.get(domain).putIfAbsent(injector.getType(), injector);
		// Check if conflict.
		if (result != null && !injector.equals(result)) {
			throw new RegistrationConflictException(this, injector.getType(), result.getName(), injector.getName(), "@QuikInjector Conflict!");
		}
		// Notify of Addition
		QuikCore.getCoreLogger().info("Registered injector for type {0} in domain \"{1}\".", injector.getType(), injector.getDomain());
		// Update Injections.
		if (used) {
			performInjectionsFor(injector);
		}
	}

	public synchronized void registerInjectables(Object container) {
		final Object obj = (container instanceof Class) ? null : container;
		ReflectionStreams.streamAccessibleFields(container)
				.filter(f -> f.isAnnotationPresent(QuikInjected.class))
				.forEach(f -> {
					try {
						registerInjectable(obj, f);
					} catch (WrapperCreationException e) {
						throw new RuntimeException(e);
					}
				});
	}

	public synchronized void registerInjectors(Object container) {
		final Object obj = (container instanceof Class) ? null : container;
		ReflectionStreams.streamAccessibleMethods(container)
				.filter(m -> m.isAnnotationPresent(QuikInjector.class))
				.forEach(m -> {
					try {
						registerInjector(obj, m);
					} catch (WrapperCreationException e) {
						throw new RuntimeException(e);
					}
				});
	}

	public synchronized QuikInjectorWrapper getInjector(Class clazz) {
		return this.injectors.get("").get(clazz);
	}

	public synchronized QuikInjectorWrapper getInjector(String domain, Class clazz) {
		QuikInjectorWrapper wrapper = null;
		if (this.injectors.containsKey(domain)) {
			wrapper = this.injectors.get(domain).get(clazz);
		}
		if (wrapper == null) {
			wrapper = this.injectors.get("").get(clazz);
		}
		return wrapper;
	}

	public synchronized void performInjections() {
		if (!this.used) {
			this.used = true;
			this.targets.values().stream()
					.flatMap(s -> s.stream())
					.forEach(this::performInjection);
		}
	}

	private synchronized void performInjection(QuikInjectedWrapper target) {
		final QuikInjectorWrapper injector = getInjector(target.getDomain(), target.getType());
		if (injector == null) {
			if (!target.isOptional()) {
				throw new Error("Error Injecting '" + target + "': no sutible injector found!");
			} else {
				QuikCore.getCoreLogger().info("Skipping optional injection for {0} as no suitable injector found!", target);
				return;
			}
		}
		injector.inject(target);
	}

	private void performInjectionsFor(QuikInjectorWrapper injector) {
		streamInjectables(injector.getDomain())
				.filter(t -> t.getType().isAssignableFrom(injector.getType()))
				.forEach(injector::inject);
	}

	private Stream<QuikInjectedWrapper> streamInjectables(String domain) {
		if (domain.isEmpty()) {
			return this.targets.values().stream().flatMap(s -> s.stream());
		} else {
			return this.targets.getOrDefault(domain, Collections.EMPTY_SET).stream();
		}
	}

}
