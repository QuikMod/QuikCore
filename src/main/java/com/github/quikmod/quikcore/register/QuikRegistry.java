/*
 */
package com.github.quikmod.quikcore.register;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.ReflectionStreams;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ryan
 */
public class QuikRegistry {

	private final Set<Method> registers;

	public QuikRegistry() {
		this.registers = new HashSet<>();
	}

	public void registerRegisters(Class<?> clazz) {
		ReflectionStreams
				.streamAccessibleMethods(clazz)
				.filter(QuikRegistry::isRegisterMethod)
				.peek(r -> QuikCore.getCoreLogger().info("Registered Register Method: {0}!", r.getName()))
				.forEach(registers::add);
	}

	public void performRegister(Class<?> clazz) {
		this.registers.forEach(r -> performRegisterInvoke(r, clazz));
	}

	private static void performRegisterInvoke(Method register, Class<?> clazz) {
		try {
			register.invoke(null, clazz);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			QuikCore.getCoreLogger().trace(e);
		}
	}

	public static boolean isRegisterMethod(Method m) {
		if (!m.isAnnotationPresent(QuikClassRegister.class)) {
			// Nothing to log here, as not important.
			return false;
		} else if (!Modifier.isStatic(m.getModifiers())) {
			QuikCore.getCoreLogger().debug("@QuikRegister methods must be static! {0} is not!", m.getName());
			return false;
		} else if (m.getParameterCount() != 1) {
			QuikCore.getCoreLogger().debug("@QuikRegister methods must accept a single argument! {0} does not!", m.getName());
			return false;
		} else if (!Class.class.isAssignableFrom(m.getParameters()[0].getType())) {
			QuikCore.getCoreLogger().debug("@QuikRegister methods must accept a class as an argument! {0} does not!", m.getName());
			return false;
		} else {
			return makeAccessable(m);
		}
	}

	public static boolean makeAccessable(Method m) {
		if (m.isAccessible()) {
			return true;
		} else {
			try {
				m.setAccessible(true);
				return true;
			} catch (SecurityException e) {
				QuikCore.getCoreLogger().info("QuikCore cannot access @quikregister method: {0}!", m.getName());
				QuikCore.getCoreLogger().debug(e.getLocalizedMessage());
				return false;
			}
		}
	}

}
