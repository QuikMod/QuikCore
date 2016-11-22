/*
 */
package com.github.quikmod.quikcore.reflection;

import com.github.quikmod.quikcore.core.QuikCore;
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
	final QuikRegisterRegistry registers;

	public QuikReflector() {
		this.reflections = new Reflections(
				new ConfigurationBuilder().setScanners(
						new SubTypesScanner(),
						new TypeAnnotationsScanner()
				).forPackages("com", "net", "org")
		);
		this.registers = new QuikRegisterRegistry();
	}

	public QuikRegisterRegistry getRegisters() {
		return registers;
	}

	public Stream<Class<?>> findQuikClasses() {
		return this.reflections.getTypesAnnotatedWith(Quik.class).stream();
	}

	public void performLoad() {
		// Register Registers
		// Yo, Dawg.. I heard you like registers!
		QuikCore.getCoreLogger().info("Registering Registers!");
		long start = System.currentTimeMillis();
		this.findQuikClasses().forEach(this.registers::registerRegisters);
		long end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Registered Registers! ({0} ms)", end - start);
		
		// Perform actual load
		QuikCore.getCoreLogger().info("Performing Load!");
		start = System.currentTimeMillis();
		this.findQuikClasses().forEach(this.registers::performRegister);
		end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Load completed! ({0} ms)", end - start);
	}

}
