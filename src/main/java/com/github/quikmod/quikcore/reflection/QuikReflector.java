/*
 */
package com.github.quikmod.quikcore.reflection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.register.QuikRegistry;
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

	private final Reflections reflections;
	private final QuikRegistry registers;
	private final QuikDomains domains;

	public QuikReflector() {
		this.reflections = new Reflections(
				new ConfigurationBuilder().setScanners(
						new SubTypesScanner(),
						new TypeAnnotationsScanner()
				).forPackages("com", "net", "org")
		);
		this.registers = new QuikRegistry();
		this.domains = new QuikDomains();
	}

	public QuikDomains getDomains() {
		return domains;
	}

	public Stream<Class<?>> findQuikClasses() {
		return this.reflections.getTypesAnnotatedWith(Quik.class).stream();
	}
	
	public Stream<Package> findQuikPackages() {
		return this.reflections.getTypesAnnotatedWith(QuikDomain.class).stream()
				.filter(c -> c.getSimpleName().equals("package-info"))
				.map(c -> c.getPackage());
	}

	public void performLoad() {
		// Start
		QuikCore.getCoreLogger().info("Starting Wizardry!");
		final long base = System.currentTimeMillis();
		
		// Register Domains
		QuikCore.getCoreLogger().info("Registering Domains!");
		long start = System.currentTimeMillis();
		this.findQuikPackages().forEach(this.domains::registerDomain);
		long end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Registered Domains! ({0} ms)", end - start);

		// Register Registers
		// Yo, Dawg.. I heard you like registers!
		QuikCore.getCoreLogger().info("Registering Registers!");
		start = System.currentTimeMillis();
		this.findQuikClasses().distinct().forEach(c -> {
			this.registers.registerRegisters(c);
			this.domains.registerDomain(c);
		});
		end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Registered Registers! ({0} ms)", end - start);

		// Perform actual load
		QuikCore.getCoreLogger().info("Performing Load!");
		start = System.currentTimeMillis();
		this.findQuikClasses().forEach(this.registers::performRegister);
		end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Load completed! ({0} ms)", end - start);

		// Fin
		end = System.currentTimeMillis();
		QuikCore.getCoreLogger().info("Wizardry Finished! ({0} ms)", end - base);
	}

}
