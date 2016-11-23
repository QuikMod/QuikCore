/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.command;

import com.github.quikmod.quikcore.util.Tokenizer;
import com.googlecode.concurrenttrees.common.KeyValuePair;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author RlonRyan
 */
public final class QuikCommandManager {
	
	public static final String MARKER = "--";
	public static final int MARKER_LENGTH = MARKER.length();
	
	private final RadixTree<QuikCommandWrapper> commands;

	/**
	 * Constructs a QuikCommand set, that uses a RadixTree for optimal searching
	 * and command suggestions.
	 */
	public QuikCommandManager() {
		this.commands = new ConcurrentRadixTree<>(new DefaultCharArrayNodeFactory());
	}

	/**
	 * Returns a lazy stream which contains the set of command names that are
	 * closest to the given input string.
	 *
	 * @param input The key to search for in the command set.
	 * @return a lazy stream with the closest matching command names.
	 */
	public Stream<String> getPossibleCommandNamesFor(String input) {
		return StreamSupport.stream(this.commands.getClosestKeys(input.toLowerCase()).spliterator(), false).map(CharSequence::toString);
	}

	/**
	 * Returns a lazy stream which contains the set of key/value commands that
	 * are closest to the given input string.
	 *
	 * @param input The key to search for in the command set.
	 * @return a lazy stream with the closest matching key/value pairs.
	 */
	public Stream<KeyValuePair<QuikCommandWrapper>> getPossibleCommandsFor(String input) {
		// Since I like streams.
		return StreamSupport.stream(this.commands.getKeyValuePairsForClosestKeys(input.toLowerCase()).spliterator(), false);
	}

	/**
	 * If a value is not already associated with the given key in the tree,
	 * associates the given value with the key; otherwise if an existing value
	 * is already associated, returns false and does not overwrite it.
	 *
	 * @param command The command to add to the command set.
	 * @return If the command was successfully associated with the set.
	 */
	public boolean registerCommand(QuikCommandWrapper command) {
		return this.commands.putIfAbsent(command.getName().toLowerCase(), command) == null;
	}

	/**
	 * Attempts to add a command to the command set. First tries to wrap the
	 * method using a QuickCommand wrapper, discarding any
	 * WrapperCreationExceptions, and then filtering the returned optional by
	 * the success of the command registration.
	 *
	 * @param method the method to add as a command to the command set.
	 * @return an optional containing the wrapped method, or an empty optional
	 * if the method was invalid, or if a command by that name already existed.
	 */
	public Optional<QuikCommandWrapper> attemptAddCommand(Method method) {
		return QuikCommandWrapper
				.attemptWrapperCreation(method)
				.filter(this::registerCommand);
	}

	/**
	 * Iterates through all the methods in a given class and attempts to add them as @QuikCommands.
	 *
	 * @param clazz The class containing possible @QuikCommands.
	 */
	public void addCommands(Class clazz) {
		for (Method m : clazz.getMethods()) {
			if (m.isAnnotationPresent(QuikCommand.class)) {
				this.attemptAddCommand(m);
			}
		}
	}
	
	public QuikInvocationResult invoke(String input) {
		Deque<String> tokens = Tokenizer.tokenize(input);
		if (tokens.isEmpty()) {
			return QuikInvocationResult.fromNothing();
		}
		List<KeyValuePair<QuikCommandWrapper>> options = this.getPossibleCommandsFor(tokens.pop()).collect(Collectors.toList());
		if (options.isEmpty()) {
			return QuikInvocationResult.fromMissing(input);
		} else if (options.size() > 1) {
			return QuikInvocationResult.fromAmbiguous(input, (String[]) options.toArray());
		} else {
			return options.get(0).getValue().invoke(mapify(tokens));
		}
	}
	
	public Map<String, String> mapify(Deque<String> input) {
		Map<String, String> args = new HashMap<>();
		while (!input.isEmpty()) {
			String token = input.pop();
			if (token.startsWith(MARKER)) {
				if (!input.isEmpty() && !input.peek().startsWith(MARKER)) {
					args.put(token.substring(MARKER_LENGTH), input.pop());
				} else {
					args.put(token, "true");
				}
			} else {
				// Place in default token slot.
				while (!input.isEmpty() && !input.peek().startsWith(MARKER)) {
					token += " " + input.pop();
				}
				args.put("", token);
			}
		}
		return args;
	}
	
}
