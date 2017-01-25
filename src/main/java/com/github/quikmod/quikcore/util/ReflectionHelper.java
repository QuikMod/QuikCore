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

	/**
	 * Iterates over all the accessible values of type T in the class, and
	 * applies the consumer to them.
	 *
	 * This method is designed to allow for reflection into any class without
	 * having to worry about exceptions.
	 *
	 * @param <T> the desired type of value to extract from the class.
	 * @param from the class or object to extract values from.
	 * @param type the class of the desired type of values to extract. Required
	 * for casting purposes.
	 * @param consumer a function accepting the extracted values.
	 */
	public static <T> void forEachValueIn(Object from, Class<T> type, Consumer<T> consumer) {
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
				QuikCore.getCoreLogger().warn(
						"ReflectionHelper.forEachIn() Skipping Field: \"{0}\" in Class: \"{1}\"!",
						field.getName(),
						from.getClass().getCanonicalName()
				);
			}
		});
	}

	/**
	 * Iterates over all the accessible values of type T with annotation of type
	 * A in the class, and applies the consumer to them.
	 *
	 * This method is designed to allow for reflection into any class without
	 * having to worry about exceptions.
	 *
	 * @param <T> the desired type of value to extract from the class.
	 * @param <A> the type of annotation desired to be present.
	 * @param from the class or object to extract values from.
	 * @param type the class of the desired type of values to extract. Required
	 * for casting purposes.
	 * @param annotation the type of annotation the extracted values should be
	 * annotated with.
	 * @param consumer a function accepting the extracted values.
	 */
	public static <T, A extends Annotation> void forEachValueIn(Object from, Class<T> type, Class<A> annotation, BiConsumer<T, A> consumer) {
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
				QuikCore.getCoreLogger().warn(
						"ReflectionHelper.forEachIn() Skipping Field: \"{0}\" in Class: \"{1}\"!",
						field.getName(),
						from.getClass().getCanonicalName()
				);
			}
		});
	}

	/**
	 * Iterates over all the accessible fields of type T with annotation A in
	 * the class, and applies the consumer to them.
	 *
	 * This method is designed to allow for reflection into any class without
	 * having to worry about exceptions.
	 *
	 * @param <A> the desired type of annotation to be present on the extracted
	 * fields.
	 * @param from the class or object to extract values from.
	 * @param annotation the class of the desired type of annotation to be
	 * present on the extracted fields. Required for casting purposes.
	 * @param consumer a function accepting the extracted fields, with
	 * respective annotations.
	 */
	public static <A extends Annotation> void forEachFieldIn(Object from, Class<A> annotation, BiConsumer<Field, A> consumer) {
		forEachFieldIn(from, (field) -> {
			if (field.isAnnotationPresent(annotation)) {
				consumer.accept(field, field.getAnnotation(annotation));
			}
		});
	}

	/**
	 * Iterates over all the accessible fields of type T in the class, and
	 * applies the consumer to them.
	 *
	 * This method is designed to allow for reflection into any class without
	 * having to worry about exceptions.
	 *
	 * @param from the class or object to extract values from.
	 * @param consumer a function accepting the extracted fields, with
	 * respective annotations.
	 */
	public static void forEachFieldIn(Object from, Consumer<Field> consumer) {
		final boolean isInstance = !(from instanceof Class);
		final Class clazz = isInstance ? from.getClass() : (Class) from;
		for (Field f : clazz.getDeclaredFields()) {
			if (isInstance || Modifier.isStatic(f.getModifiers())) {
				consumer.accept(f);
			}
		}
	}

	/**
	 * Creates a stream of all the methods present in a given object or class
	 * that are accessible in that context. I.e. if an object is passed then the
	 * stream will include instance methods, but if a class is passed then
	 * instance methods will not be included.
	 *
	 * @param from the object or class to stream the methods from.
	 * @return a stream containing all the accessible methods for the class or object.
	 */
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
			// Essentially, no.
		} catch (NoSuchMethodException e) {
			// This would just be no.
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
