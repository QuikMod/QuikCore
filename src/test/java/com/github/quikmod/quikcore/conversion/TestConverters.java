/*
 */
package com.github.quikmod.quikcore.conversion;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.defaults.QuikDefaultConfig;
import com.github.quikmod.quikcore.defaults.QuikDefaultLog;
import com.github.quikmod.quikcore.defaults.QuikDefaultNetwork;
import com.github.quikmod.quikcore.defaults.QuikDefaultTranslator;
import java.nio.file.Paths;
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
		QuikCore.init(new QuikDefaultLog(), new QuikDefaultTranslator(), new QuikDefaultConfig(Paths.get("config")), new QuikDefaultNetwork());
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
		assertEquals(1, (int) QuikCore.getConverters().convert(Integer.class, "1").get());
		assertEquals(1.01f, (float) QuikCore.getConverters().convert(Float.class, "1.01").get(), 0);
		assertEquals(1.01, (double) QuikCore.getConverters().convert(Double.class, "1.01").get(), 0);
		assertEquals("1.01", QuikCore.getConverters().convert(String.class, "1.01").get());
	}

	@Test
	public void testMissingConverter() {
		assertEquals(Optional.empty(), QuikCore.getConverters().convert(TestConverters.class, "Does it matter?"));
	}

}
