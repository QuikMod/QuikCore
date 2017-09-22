/*
 */
package com.github.quikmod.quikcore.injection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.reflection.Quik;
import com.github.quikmod.quikcore.reflection.QuikRegister;
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
@Quik
public final class QuikInjectorManager {

    private static final Map<String, Set<QuikInjectedWrapper>> TARGETS;
    private static final Map<String, Map<Class, QuikInjectorWrapper>> INJECTORS;

    private static boolean injectionsOccurred = false;

    static {
        // Initialize.
        TARGETS = new HashMap<>();
        INJECTORS = new HashMap<>();
        // Add root slot.
        INJECTORS.put("", new HashMap<>());
    }

    private QuikInjectorManager() {
        // Nothing to see here.
    }

    public static synchronized void registerInjectable(Object container, Field site) throws WrapperCreationException {
        // Create Injectable Wrapper.
        final QuikInjectedWrapper target = new QuikInjectedWrapper(container, site);
        // Ensure Domain Set Exists.
        if (!TARGETS.containsKey(target.getDomain())) {
            TARGETS.put(target.getDomain(), new HashSet<>());
        }
        // Insert Injectable.
        TARGETS.get(target.getDomain()).add(target);
        // Notify
        QuikCore.getCoreLogger().info("Registered Injection Site: {0}.", target.getName());
        // Update Injection.
        if (injectionsOccurred) {
            performInjection(target);
        }
    }

    public static synchronized void registerInjector(Object container, Method m) throws WrapperCreationException {
        // Create Wrapper
        final QuikInjectorWrapper injector = new QuikInjectorWrapper(container, m);
        final String domain = injector.getDomain();
        // Ensure Domain Map Exists.
        if (!INJECTORS.containsKey(domain)) {
            INJECTORS.put(domain, new HashMap());
        }
        // Add the wrapper.
        final QuikInjectorWrapper result = INJECTORS.get(domain).putIfAbsent(injector.getType(), injector);
        // Check if conflict.
        if (result != null && !injector.equals(result)) {
            throw new RegistrationConflictException("QuikInjectorManager", injector.getType(), result.getName(), injector.getName(), "@QuikInjector Conflict!");
        }
        // Notify of Addition
        QuikCore.getCoreLogger().info("Registered injector for type {0} in domain \"{1}\".", injector.getType(), injector.getDomain());
        // Update Injections.
        if (injectionsOccurred) {
            performInjectionsFor(injector);
        }
    }

    @QuikRegister
    public static synchronized void registerInjectables(Object container) {
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

    @QuikRegister
    public static synchronized void registerInjectors(Object container) {
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

    public static synchronized QuikInjectorWrapper getInjector(Class clazz) {
        return INJECTORS.get("").get(clazz);
    }

    public static synchronized QuikInjectorWrapper getInjector(String domain, Class clazz) {
        QuikInjectorWrapper wrapper = null;
        if (INJECTORS.containsKey(domain)) {
            wrapper = INJECTORS.get(domain).get(clazz);
        }
        if (wrapper == null) {
            wrapper = INJECTORS.get("").get(clazz);
        }
        return wrapper;
    }

    public static synchronized void performInjections() {
        if (!injectionsOccurred) {
            injectionsOccurred = true;
            TARGETS.values().stream()
                    .flatMap(s -> s.stream())
                    .forEach(QuikInjectorManager::performInjection);
        }
    }

    private static synchronized void performInjection(QuikInjectedWrapper target) {
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

    private static void performInjectionsFor(QuikInjectorWrapper injector) {
        streamInjectables(injector.getDomain())
                .filter(t -> t.getType().isAssignableFrom(injector.getType()))
                .forEach(injector::inject);
    }

    private static Stream<QuikInjectedWrapper> streamInjectables(String domain) {
        if (domain.isEmpty()) {
            return TARGETS.values().stream().flatMap(s -> s.stream());
        } else {
            return TARGETS.getOrDefault(domain, Collections.EMPTY_SET).stream();
        }
    }

}
