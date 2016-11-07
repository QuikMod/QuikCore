/*
 */
package com.github.quikmod.quiklib.test;

import com.github.quikmod.quiklib.core.QuikCore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author RlonRyan
 */
public class TestConverters {

	public TestConverters() {
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
		Assert.assertEquals(1, (int)QuikCore.getConverters().convert(Integer.class, "1").get());
		Assert.assertEquals(1.01f, (float)QuikCore.getConverters().convert(Float.class, "1.01").get(), 0);
		Assert.assertEquals(1.01, (double)QuikCore.getConverters().convert(Double.class, "1.01").get(), 0);
		Assert.assertEquals("1.01", QuikCore.getConverters().convert(String.class, "1.01").get());
	}

}
