/*
 */
package com.github.quikmod.quikcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
public class WrapperCreationExceptionTest {
	
	public static final int TEST_FIELD = 0;
	
	public WrapperCreationExceptionTest() {
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
	
	public void testMethod(String s) {
		// NOPE
	}

	@Test
	public void testExceptionCreation() throws NoSuchFieldException, NoSuchMethodException, SecurityException {
		Field f = this.getClass().getDeclaredField("TEST_FIELD");
		Method m = this.getClass().getDeclaredMethod("testMethod", String.class);
		Parameter p = m.getParameters()[0];
		
		System.out.println(new WrapperCreationException(f, "Test reason.").toString());
		System.out.println(new WrapperCreationException(m, "Test reason.").toString());
		System.out.println(new WrapperCreationException(m, p, "Test reason").toString());
	}
	
}
