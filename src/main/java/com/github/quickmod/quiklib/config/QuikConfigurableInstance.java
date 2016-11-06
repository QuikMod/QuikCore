/*
 */
package com.github.quickmod.quiklib.config;

/**
 * Interface for AgriConfigurable instances that need name resolution based on
 * the instance.
 *
 * @author RlonRyan
 */
public interface QuikConfigurableInstance {

	String resolve(String input);

}
