/*
 */
package com.github.quikmod.quikcore.reflection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.util.ReflectionHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ryan
 */
public class QuikRegisterRegistry {

	private final Set<Method> registers;

	public QuikRegisterRegistry() {
		this.registers = new HashSet<>();
	}

	public void registerRegisters(Class<?> clazz) {
		ReflectionHelper
				.streamMethods(clazz)
				.filter(QuikRegisterRegistry::isRegisterMethod)
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
		return m.isAnnotationPresent(QuikRegister.class)
				&& m.getParameterCount() == 1
				&& Class.class.isAssignableFrom(m.getParameters()[0].getType())
				&& makeAccessable(m);
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
