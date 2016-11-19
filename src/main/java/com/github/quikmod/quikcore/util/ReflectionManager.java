/*
 */
package com.github.quikmod.quikcore.util;

import org.reflections.Reflections;

/**
 *
 * @author Ryan
 */
public final class ReflectionManager {

	private static Reflections REFLECTIONS = new Reflections("com.github.quikmod");

	public static Reflections getReflections() {
		return REFLECTIONS;
	}

	public static Reflections getReflectionsWith(String pkg) {
		synchronized (REFLECTIONS) {
			REFLECTIONS = REFLECTIONS.merge(new Reflections(pkg));
		}
		return REFLECTIONS;
	}

}
