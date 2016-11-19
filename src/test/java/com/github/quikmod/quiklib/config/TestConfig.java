/*
 */
package com.github.quikmod.quiklib.config;

import com.github.quikmod.quiklib.core.QuikCore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author RlonRyan
 */
public class TestConfig {

	public TestConfig() {
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
	// @Test
	// public void hello() {}
	@Test
	public void testConfig() {
		QuikCore.getConfig().load();
		QuikCore.getCoreLogger().info(QuikCore.getConfig().toString());
		QuikCore.getCoreLogger().info(DummyConfig.asString());
		QuikCore.getCoreLogger().info(DummyConfig.asString());
		QuikCore.getConfig().save();
	}

}
