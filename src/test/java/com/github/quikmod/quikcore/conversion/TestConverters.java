/*
 */
package com.github.quikmod.quikcore.conversion;

import com.github.quikmod.quikcore.core.QuikCore;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author RlonRyan
 */
public class TestConverters {

	public TestConverters() {
	}

	@BeforeClass
	public static void setUpClass() {
        try {
            QuikCore.setup();
        } catch (Exception e) {
            // Doesn't matter.
        }
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
	public void testConvert() {
		assertEquals(1, (int) QuikConverterManager.convert(Integer.class, "1").get());
		assertEquals(1.01f, (float) QuikConverterManager.convert(Float.class, "1.01").get(), 0);
		assertEquals(1.01, (double) QuikConverterManager.convert(Double.class, "1.01").get(), 0);
		assertEquals("1.01", QuikConverterManager.convert(String.class, "1.01").get());
	}

	@Test
	public void testMissingConverter() {
		assertEquals(Optional.empty(), QuikConverterManager.convert(TestConverters.class, "Does it matter?"));
	}

}
