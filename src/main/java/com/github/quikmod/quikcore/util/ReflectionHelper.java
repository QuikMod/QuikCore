/*
 */
package com.github.quikmod.quikcore.util;

import com.github.quikmod.quikcore.core.QuikCore;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A class to aid in the execution of reflection magic.
 *
 * @author RlonRyan
 */
public class ReflectionHelper {

	// Dummy Constructor.
	private ReflectionHelper() {
	}

	public static <T> void forEachIn(Object from, Class<T> type, Consumer<T> consumer) {
		final Object target = from instanceof Class ? null : from;
		forEachFieldIn(from, (field) -> {
			try {
				field.setAccessible(true);
				Object obj = field.get(target);
				if (obj != null && type.isAssignableFrom(obj.getClass())) {
					consumer.accept(type.cast(obj));
				}
			} catch (IllegalAccessException e) {
				// Oh well...
				QuikCore.getLogger("AgriCraft").warn(
						"ReflectionHelper.forEachIn() Skipping Field: \"{0}\" in Class: \"{1}\"!",
						field.getName(),
						from.getClass().getCanonicalName()
				);
			}
		});
	}

	public static <T, A extends Annotation> void forEachIn(Object from, Class<T> type, Class<A> annotation, BiConsumer<T, A> consumer) {
		final Object target = from instanceof Class ? null : from;
		ReflectionHelper.forEachFieldIn(from, annotation, (field, anno) -> {
			try {
				field.setAccessible(true);
				Object obj = field.get(target);
				if (obj != null && type.isAssignableFrom(obj.getClass())) {
					consumer.accept(type.cast(obj), anno);
				}
			} catch (IllegalAccessException e) {
				// Oh well...
				QuikCore.getLogger("AgriCraft").warn(
						"ReflectionHelper.forEachIn() Skipping Field: \"{0}\" in Class: \"{1}\"!",
						field.getName(),
						from.getClass().getCanonicalName()
				);
			}
		});
	}

	public static <A extends Annotation> void forEachFieldIn(Object from, Class<A> annotation, BiConsumer<Field, A> consumer) {
		forEachFieldIn(from, (field) -> {
			if (field.isAnnotationPresent(annotation)) {
				consumer.accept(field, field.getAnnotation(annotation));
			}
		});
	}

	public static void forEachFieldIn(Object from, Consumer<Field> consumer) {
		final boolean isInstance = !(from instanceof Class);
		final Class clazz = isInstance ? from.getClass() : (Class) from;
		for (Field f : clazz.getDeclaredFields()) {
			if (isInstance || Modifier.isStatic(f.getModifiers())) {
				consumer.accept(f);
			}
		}
	}

	public static Stream<Method> streamMethods(Object from) {
		final boolean isInstance = !(from instanceof Class);
		final Class clazz = isInstance ? from.getClass() : (Class) from;
		final Stream<Method> methods = Arrays.stream(clazz.getDeclaredMethods());
		return isInstance ? methods : methods.filter(m -> Modifier.isStatic(m.getModifiers()));
	}

	public static boolean hasConstructorFor(Class<?> clazz, Class<?>... types) {
		try {
			Constructor<?> constructor = clazz.getConstructor(types);
			return constructor != null;
		} catch (SecurityException e) {
			
		} catch (NoSuchMethodException e) {

		}
		return false;
	}

	public static <T> Optional<T> attemptInstantiate(Class<T> clazz, Object... parameters) {
		Class types[] = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			types[i] = parameters[i].getClass();
		}
		try {
			return Optional.of(clazz.getConstructor(types).newInstance(parameters));
		} catch (SecurityException | IllegalAccessException e) {
			QuikCore.getCoreLogger().debug("Unable to access constructor for class {0}!", clazz.getName());
		} catch (IllegalArgumentException e) {
			QuikCore.getCoreLogger().error("The following error should not have occured!");
			QuikCore.getCoreLogger().trace(e);
		} catch (InstantiationException e) {
			QuikCore.getCoreLogger().debug("Unable to instantiate class {0}!\nGiven reason is \"{1}\"!", clazz.getName(), e.getMessage());
		} catch (InvocationTargetException e) {
			QuikCore.getCoreLogger().debug("The constructor for class {0} threw an error!\nGiven reason is: \"{1}\"!", clazz.getName(), e.getCause());
		} catch (NoSuchMethodException e) {
			QuikCore.getCoreLogger().debug("The class {0} does not provide a constructor with parameters of types: {1}!", clazz.getName(), Arrays.asList(types));
		}
		return Optional.empty();
	}

}
