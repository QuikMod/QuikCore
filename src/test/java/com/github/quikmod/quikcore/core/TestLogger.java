/*
 */
package com.github.quikmod.quikcore.core;

import com.github.quikmod.quikcore.log.QuikLogger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author RlonRyan
 */
public class TestLogger {
	
	public TestLogger() {
	}
	
	@BeforeClass
	public static void setUpClass() {
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

	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	@Test
	public void hello() {
		QuikLogger logger = QuikCore.getLogger("HelloTest");
		logger.all("log_test_key", "Hello", "All", "Hello!");
		logger.info("log_test_key", "Hello", "Info", "Hello!");
		logger.debug("log_test_key", "Hello", "Debug", "Hello!");
		logger.warn("log_test_key", "Hello", "Warn", "Hello!");
		logger.error("log_test_key", "Hello", "Error", "Hello!");
		logger.severe("log_test_key", "Hello", "Severe", "Hello!");
	}
	
}
