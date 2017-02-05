/*
 */
package com.github.quikmod.quikcore.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class TypeHelperTest {
	
	public TypeHelperTest() {
		// To get 100% coverage.
		new TypeHelper();
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
	 * Test of asArray method, of class TypeHelper.
	 */
	@Test
	public void testAsArray() {
		System.out.println("asArray");
		String[] expResult = new String[]{"One, Two, Three"};
		String[] result = TypeHelper.asArray(Arrays.asList(expResult), String.class);
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of asList method, of class TypeHelper.
	 */
	@Test
	public void testAsList() {
		System.out.println("asList");
		Object[] elements = new Object[]{"Testing", 1, 2, 3};
		List expResult = Arrays.asList(elements);
		List result = TypeHelper.asList(elements);
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of asDeque method, of class TypeHelper.
	 */
	@Test
	public void testAsDeque() {
		System.out.println("asDeque");
		Object[] elements = new Object[]{"Testing", 1, 2, 3};
		Deque expected = new ArrayDeque(Arrays.asList(elements));
		Deque result = TypeHelper.asDeque(elements);
		assertTrue(TypeHelper.areDequesEqual(expected, result));
	}

	/**
	 * Test of asSet method, of class TypeHelper.
	 */
	@Test
	public void testAsSet() {
		System.out.println("asSet");
		Object[] elements = new Object[]{"Testing", 1, 2, 3};
		Set expResult = new HashSet(Arrays.asList(elements));
		Set result = TypeHelper.asSet(elements);
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of areDequesEqual method, of class TypeHelper.
	 */
	@Test
	public void testAreDequesEqual() {
		System.out.println("areDequesEqual");
		
		// Test Empty
		assertTrue(TypeHelper.areDequesEqual(new ArrayDeque<>(), new ArrayDeque<>()));
		
		// Test Equal
		assertTrue(TypeHelper.areDequesEqual(TypeHelper.asDeque(1), TypeHelper.asDeque(1)));
		assertTrue(TypeHelper.areDequesEqual(TypeHelper.asDeque(1, 2, 3, "4"), TypeHelper.asDeque(1, 2, 3, "4")));
		
		// Test Different Element
		assertFalse(TypeHelper.areDequesEqual(TypeHelper.asDeque(0), TypeHelper.asDeque(1)));
		assertFalse(TypeHelper.areDequesEqual(TypeHelper.asDeque(0, 1), TypeHelper.asDeque(1, 0)));
		assertFalse(TypeHelper.areDequesEqual(TypeHelper.asDeque(1, 2, 3, "5", 100), TypeHelper.asDeque(1, 2, 3, "4", 100)));
		
		// Test Different Cardinality
		assertFalse(TypeHelper.areDequesEqual(new ArrayDeque<>(), TypeHelper.asDeque(1)));
		assertFalse(TypeHelper.areDequesEqual(TypeHelper.asDeque(1), TypeHelper.asDeque(1, 0)));
		assertFalse(TypeHelper.areDequesEqual(TypeHelper.asDeque(1, 2, 3, "4"), TypeHelper.asDeque(1, 2, 3, "4", 100)));
	}

	/**
	 * Test of addAll method, of class TypeHelper.
	 */
	@Test
	public void testAddAll() {
		System.out.println("addAll");
		Object[] elements = new Object[] {"Testing", "Testing", "Testing"};
		Object[] added = new Object[]{1, 2, 3};
		Collection collection = new ArrayList(Arrays.asList(elements));
		Collection expResult = new ArrayList(Arrays.asList(elements));
		expResult.addAll(Arrays.asList(added));
		assertEquals(expResult, TypeHelper.addAll(collection, added));
	}

	/**
	 * Test of isAllTypes method, of class TypeHelper.
	 */
	@Test
	public void testIsAllTypes() {
		System.out.println("isAllTypes");
		assertTrue(TypeHelper.isAllTypes("My magical string.", Object.class));
		assertTrue(TypeHelper.isAllTypes("My magical string.", String.class));
		assertTrue(TypeHelper.isAllTypes("My magical string.", String.class, Object.class));
		assertFalse(TypeHelper.isAllTypes("My magical string.", String.class, Object.class, Number.class));
		assertFalse(TypeHelper.isAllTypes("My magical string.", String.class, Number.class));
		assertFalse(TypeHelper.isAllTypes("My magical string.", Object.class, Number.class));
		assertFalse(TypeHelper.isAllTypes("My magical string.", Number.class));
	}

	/**
	 * Test of isAnyType method, of class TypeHelper.
	 */
	@Test
	public void testIsAnyType() {
		System.out.println("isAnyType");
		assertTrue(TypeHelper.isAnyType("My magical string.", Object.class));
		assertTrue(TypeHelper.isAnyType("My magical string.", String.class));
		assertTrue(TypeHelper.isAnyType("My magical string.", String.class, Object.class));
		assertTrue(TypeHelper.isAnyType("My magical string.", String.class, Object.class, Number.class));
		assertTrue(TypeHelper.isAnyType("My magical string.", String.class, Number.class));
		assertTrue(TypeHelper.isAnyType("My magical string.", Object.class, Number.class));
		assertFalse(TypeHelper.isAnyType("My magical string.", Number.class));
		assertFalse(TypeHelper.isAnyType("My magical string.", Number.class, int.class));
	}

	/**
	 * Test of isType method, of class TypeHelper.
	 */
	@Test
	public void testIsType() {
		System.out.println("isType");
		
		// Primative
		assertTrue(TypeHelper.isType(0, Integer.class));
		assertTrue(TypeHelper.isType(0, Object.class));
		assertTrue(TypeHelper.isType(0, Number.class));
		assertFalse(TypeHelper.isType(0, String.class));
		
		// Object
		assertTrue(TypeHelper.isType("STRING", String.class));
		assertTrue(TypeHelper.isType("STRING", Object.class));
		assertFalse(TypeHelper.isType("STRING", Number.class));
		assertFalse(TypeHelper.isType("STRING", Boolean.class));
	}

	/**
	 * Test of cast method, of class TypeHelper.
	 */
	@Test
	public void testCast() {
		System.out.println("cast");
		assertFalse(TypeHelper.cast(null, String.class).isPresent());
		assertFalse(TypeHelper.cast(1, String.class).isPresent());
		assertTrue(TypeHelper.cast("Yup", String.class).isPresent());
	}

	/**
	 * Test of advance method, of class TypeHelper.
	 */
	@Test
	public void testAdvance() {
		System.out.println("advance");
		assertEquals(String.class, TypeHelper.advance(String.class));
		assertEquals(Boolean.class, TypeHelper.advance(boolean.class));
		assertEquals(Boolean.class, TypeHelper.advance(Boolean.class));
		assertEquals(Byte.class, TypeHelper.advance(byte.class));
		assertEquals(Byte.class, TypeHelper.advance(Byte.class));
		assertEquals(Character.class, TypeHelper.advance(char.class));
		assertEquals(Character.class, TypeHelper.advance(Character.class));
		assertEquals(Short.class, TypeHelper.advance(short.class));
		assertEquals(Short.class, TypeHelper.advance(Short.class));
		assertEquals(Integer.class, TypeHelper.advance(int.class));
		assertEquals(Integer.class, TypeHelper.advance(Integer.class));
		assertEquals(Long.class, TypeHelper.advance(long.class));
		assertEquals(Long.class, TypeHelper.advance(Long.class));
		assertEquals(Float.class, TypeHelper.advance(float.class));
		assertEquals(Float.class, TypeHelper.advance(Float.class));
		assertEquals(Double.class, TypeHelper.advance(double.class));
		assertEquals(Double.class, TypeHelper.advance(Double.class));
		assertEquals(Void.class, TypeHelper.advance(void.class));
		assertEquals(Void.class, TypeHelper.advance(Void.class));
	}
	
}
