/*
 */
package com.github.quikmod.quikcore.reflection;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.defaults.QuikDefaultConfig;
import com.github.quikmod.quikcore.defaults.QuikDefaultLog;
import com.github.quikmod.quikcore.defaults.QuikDefaultTranslator;
import com.github.quikmod.quikcore.injection.QuikInjected;
import com.github.quikmod.quikcore.injection.QuikInjector;
import com.github.quikmod.quikcore.log.QuikLogger;
import java.nio.file.Paths;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ryan
 */
@Quik
public class InjectorTest {
	
	@QuikInjected
	private static QuikLogger domainLog;
	
	@QuikInjected
	private static Random soRandom;
	
	public InjectorTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		QuikCore.init(new QuikDefaultLog(), new QuikDefaultTranslator(), new QuikDefaultConfig(Paths.get("config")));
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testLoggerInjection() {
		System.out.println("testLoggerInjection");
		domainLog.info("Injector success!");
	}
	
	@Test
	public void testRandomInjection() {
		System.out.println("testRandomInjection");
		System.out.println("Random Integer: " + soRandom.nextInt());
	}
	
	@QuikInjector
	public static Random randomizer(String domain) {
		return new Random();
	}
	
}
