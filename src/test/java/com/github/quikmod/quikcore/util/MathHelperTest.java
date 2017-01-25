/*
 */
package com.github.quikmod.quikcore.util;

import java.util.AbstractMap;
import java.util.Map;
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
public class MathHelperTest {
	
	public MathHelperTest() {
		new MathHelper();
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

	/**
	 * Test of inRange method, of class MathHelper.
	 */
	@Test
	public void testInRangeInt() {
		assertEquals(0, MathHelper.inRange(-1, 0, 10));
		assertEquals(5, MathHelper.inRange(5, 0, 10));
		assertEquals(10, MathHelper.inRange(11, 0, 10));
	}

	/**
	 * Test of inRange method, of class MathHelper.
	 */
	@Test
	public void testInRangeFloat() {
		assertEquals(0.0f, MathHelper.inRange(-1.0f, 0.0f, 1.0f), 0.0f);
		assertEquals(0.5f, MathHelper.inRange(0.5f, 0.0f, 1.0f), 0.0f);
		assertEquals(1.0f, MathHelper.inRange(2.0f, 0.0f, 1.0f), 0.0f);
	}

	/**
	 * Test of inRange method, of class MathHelper.
	 */
	@Test
	public void testInRangeDouble() {
		assertEquals(0.0, MathHelper.inRange(-1.0, 0.0, 1.0), 0.0);
		assertEquals(0.5, MathHelper.inRange(0.5, 0.0, 1.0), 0.0);
		assertEquals(1.0, MathHelper.inRange(2.0, 0.0, 1.0), 0.0);
	}

	/**
	 * Test of entryOf method, of class MathHelper.
	 */
	@Test
	public void testEntryOf() {
		Object key = "KEY";
		Object value = "VALUE";
		Map.Entry expResult = new AbstractMap.SimpleEntry(key, value);
		Map.Entry result = MathHelper.entryOf(key, value);
		assertEquals(expResult, result);
	}
	
}
