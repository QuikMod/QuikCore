/*
 */
package com.github.quikmod.quiklib.command;

import com.github.quikmod.quiklib.core.QuikCore;
import com.github.quikmod.quiklib.util.ReflectionHelper;
import com.github.quikmod.quiklib.util.WrapperCreationException;
import com.googlecode.concurrenttrees.common.KeyValuePair;
import java.lang.reflect.Method;
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
		QuikCore.getCoreLogger().info("Preparing for CommandManager test.");
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
	 */
	@Test
	public void testRegisterCommand() throws WrapperCreationException {
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
	 */
	@Test
	public void testInvoke() throws WrapperCreationException {
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
				"three --arg test --rand 1 --nop -1"
		);
		
		
		for (String e : testCommands) {
			System.out.println(instance.invoke(e));
		}
	}

	public static Method getCommand(String name) {
		// We actually do want this to throw a null pointer exception in the case that the test is borked.
		return ReflectionHelper
				.streamMethods(QuikCommandManagerTest.class)
				.filter(m -> m.getName().equalsIgnoreCase(name))
				.findAny()
				.get();
	}

	@QuikCommand(name = "Zero", info = "Test command with no arguments. This is the most basic command possible.")
	public static void zero() {
		System.out.println("Command 'Zero' invokation success!");
	}

	@QuikCommand(name = "nada", info = "Test command with no arguments. This is the most basic command possible.")
	public static void nada() {
		System.out.println("Command 'Nada' invokation success!");
	}

	@QuikCommand(name = "one", info = "Test command with a single argument. This is the second most basic command possible.")
	public static void one(
			@QuikParam(tag = "arg", info = "Arg") String arg
	) {
		System.out.println("Command 'One' invokation success with string parameter \"" + arg + "\"!");
	}
	
	@QuikCommand(name = "two", info = "Test command with more arguments.")
	public static void two(
			@QuikParam(tag = "arg", info = "Arg") String arg,
			@QuikParam(tag = "rand", info = "Number") int rand
	) {
		System.out.println("Command 'One' invokation success with parameters: \"" + arg + "\" & " + rand + "!");
	}
	
	@QuikCommand(name = "three", info = "Test command with even more arguments.")
	public static void three(
			@QuikParam(tag = "arg", info = "Arg") String arg,
			@QuikParam(tag = "rand", info = "Number") int rand,
			@QuikParam(tag = "nop", info = "Integer") Integer nop
	) {
		System.out.println("Command 'One' invokation success with parameters: \"" + arg + "\" & " + rand + " & " + nop + "!");
	}

}
