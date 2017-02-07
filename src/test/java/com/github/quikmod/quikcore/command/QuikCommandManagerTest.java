/*
 */
package com.github.quikmod.quikcore.command;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.defaults.QuikDefaultConfig;
import com.github.quikmod.quikcore.defaults.QuikDefaultLog;
import com.github.quikmod.quikcore.defaults.QuikDefaultTranslator;
import com.github.quikmod.quikcore.reflection.exceptions.UnknownQuikDomainException;
import com.github.quikmod.quikcore.util.ReflectionStreams;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import com.googlecode.concurrenttrees.common.KeyValuePair;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
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
public class QuikCommandManagerTest {

	public QuikCommandManagerTest() {
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

	/**
	 * Test of getPossibleCommandNamesFor method, of class QuikCommandManager.
	 */
	@Test
	public void testGetPossibleCommandNamesFor() {
		System.out.println("getPossibleCommandNamesFor");

		// Setup
		QuikCommandManager instance = new QuikCommandManager();
		instance.attemptAddCommand(getCommand("zero"));
		instance.attemptAddCommand(getCommand("nada"));
		Stream<String> possibles = instance.getPossibleCommandNamesFor("");

		// Print out info.
		possibles = possibles.peek(e -> System.out.println("Command: " + e));

		// Do tests
		assertEquals(2, possibles.count());
	}

	/**
	 * Test of getPossibleCommandsFor method, of class QuikCommandManager.
	 */
	@Test
	public void testGetPossibleCommandsFor() {
		System.out.println("getPossibleCommandsFor");

		// Setup
		QuikCommandManager instance = new QuikCommandManager();
		instance.attemptAddCommand(getCommand("zero"));
		instance.attemptAddCommand(getCommand("nada"));
		Stream<KeyValuePair<QuikCommandWrapper>> possibles = instance.getPossibleCommandsFor("");

		// Do tests
		assertEquals(2, possibles.count());
	}

	/**
	 * Test of registerCommand method, of class QuikCommandManager.
	 *
	 * @throws WrapperCreationException if the test method could not be wrapped.
	 * @throws UnknownQuikDomainException if the test method has no domain.
	 */
	@Test
	public void testRegisterCommand() throws WrapperCreationException, UnknownQuikDomainException {
		System.out.println("registerCommand");

		// Setup
		QuikCommandWrapper command1 = new QuikCommandWrapper(getCommand("zero"));
		QuikCommandWrapper command2 = new QuikCommandWrapper(getCommand("nada"));
		QuikCommandManager instance = new QuikCommandManager();

		// Do test
		assertEquals(true, instance.registerCommand(command1));
		assertEquals(false, instance.registerCommand(command1));

		assertEquals(true, instance.registerCommand(command2));
		assertEquals(false, instance.registerCommand(command2));

		assertEquals(false, instance.registerCommand(command1));
		assertEquals(false, instance.registerCommand(command2));
	}

	/**
	 * Test of attemptAddCommand method, of class QuikCommandManager.
	 */
	@Test
	public void testAttemptAddCommand() {
		System.out.println("attemptAddCommand");

		// Setup
		Method command1 = getCommand("zero");
		Method command2 = getCommand("nada");
		QuikCommandManager instance = new QuikCommandManager();

		// Do test
		assertEquals(true, instance.attemptAddCommand(command1).isPresent());
		assertEquals(false, instance.attemptAddCommand(command1).isPresent());

		assertEquals(true, instance.attemptAddCommand(command2).isPresent());
		assertEquals(false, instance.attemptAddCommand(command2).isPresent());

		assertEquals(false, instance.attemptAddCommand(command1).isPresent());
		assertEquals(false, instance.attemptAddCommand(command2).isPresent());
	}

	/**
	 * Test of attemptAddCommand method, of class QuikCommandManager.
	 *
	 * @throws WrapperCreationException if the test command could not be
	 * instantiated.
	 * @throws UnknownQuikDomainException if the test command has no domain.
	 */
	@Test
	public void testInvoke() throws WrapperCreationException, UnknownQuikDomainException {
		System.out.println("attemptAddCommand");

		// Setup
		QuikCommandManager instance = new QuikCommandManager();
		instance.registerCommand(new QuikCommandWrapper(getCommand("zero")));
		instance.registerCommand(new QuikCommandWrapper(getCommand("nada")));
		instance.registerCommand(new QuikCommandWrapper(getCommand("one")));
		instance.registerCommand(new QuikCommandWrapper(getCommand("two")));
		instance.registerCommand(new QuikCommandWrapper(getCommand("three")));

		// Define Test Strings
		List<String> testCommands = Arrays.asList(
				"zero",
				"nada",
				"one test two --nope --noper",
				"two --arg test --rand 1",
				"three --arg test --rand 1 --nop -1",
				"t --arg test --rand 1",
				"tw --arg test --rand 1",
				"Nope Noper Nopest"
		);
		
		// Perform Invokations
		for (String e : testCommands) {
			QuikInvocationResult res = instance.invoke(e);
			System.out.printf("\nCommand Invocation:\n\t- Input: %1$s\n\t- Status: %2$s\n\t- Output: %3$s\n", e, res.type, res.output);
		}
		
		// New Line
		System.out.println();
	}

	public static Method getCommand(String name) {
		// We actually do want this to throw a null pointer exception in the case that the test is borked.
		return ReflectionStreams
				.streamAccessibleMethods(QuikCommandManagerTest.class)
				.filter(m -> m.getName().equalsIgnoreCase(name))
				.findAny()
				.get();
	}

	@QuikCommand(name = "Zero", info = "Test command with no arguments. This is the most basic command possible.")
	public static String zero() {
		return "Command 'Zero' invokation success!";
	}

	@QuikCommand(name = "nada", info = "Test command with no arguments. This is the most basic command possible.")
	public static String nada() {
		return "Command 'Nada' invokation success!";
	}

	@QuikCommand(name = "one", info = "Test command with a single argument. This is the second most basic command possible.")
	public static String one(
			@QuikParam(tag = "arg", info = "Arg") String arg
	) {
		return "Command 'One' invokation success with string parameter \"" + arg + "\"!";
	}
	
	@QuikCommand(name = "two", info = "Test command with more arguments.")
	public static String two(
			@QuikParam(tag = "arg", info = "Arg") String arg,
			@QuikParam(tag = "rand", info = "Number") int rand
	) {
		return "Command 'One' invokation success with parameters: \"" + arg + "\" & " + rand + "!";
	}
	
	@QuikCommand(name = "three", info = "Test command with even more arguments.")
	public static String three(
			@QuikParam(tag = "arg", info = "Arg") String arg,
			@QuikParam(tag = "rand", info = "Number") int rand,
			@QuikParam(tag = "nop", info = "Integer") Integer nop
	) {
		return "Command 'One' invokation success with parameters: \"" + arg + "\" & " + rand + " & " + nop + "!";
	}

}
