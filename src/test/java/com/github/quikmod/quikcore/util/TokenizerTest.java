/*
 */
package com.github.quikmod.quikcore.util;

import java.util.Deque;
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
public class TokenizerTest {

	public TokenizerTest() {
		System.out.println("Tokenizer Instance: " + new Tokenizer());
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
	 * Test of tokenize method, of class Tokenizer.
	 */
	@Test
	public void testTokenize() {
		System.out.println("tokenize");
		final String input = "\"Testing This\" 1 2    3\t4";
		final Deque<String> expected = TypeHelper.asDeque("\"Testing This\"", "1", "2", "3", "4");
		assertTrue(TypeHelper.areDequesEqual(expected, Tokenizer.tokenize(input)));
	}

}
