/*
 */
package com.github.quikmod.quikcore.reflection;

import com.github.quikmod.quikcore.core.QuikCore;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author Ryan
 */
public final class QuikReflector {

	final Reflections reflections;

	final List<Consumer> loaders;

	public QuikReflector() {
		this.reflections = new Reflections(
				new ConfigurationBuilder().setScanners(
						new SubTypesScanner(),
						new TypeAnnotationsScanner()
				).forPackages("")
		);
		this.loaders = new ArrayList<>();
	}

	public Stream<Class<?>> findQuikClasses() {
		return this.reflections.getTypesAnnotatedWith(Quik.class).stream();
	}

	public void registerLoader(Consumer<Class<?>> loader) {
		if (!this.loaders.contains(loader)) {
			this.loaders.add(loader);
		}
	}

	public void performLoad() {
		QuikCore.getCoreLogger().info("Performing Load!");
		final long start = System.currentTimeMillis();
		this.findQuikClasses().forEach(c -> this.loaders.forEach(l -> l.accept(c)));
		final long end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Load completed! ({0} ms)", end - start);
	}

}
