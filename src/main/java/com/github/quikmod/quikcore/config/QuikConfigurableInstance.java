/*
 */
package com.github.quikmod.quikcore.config;

/**
 * Interface for AgriConfigurable instances that need name resolution based on
 * the instance.
 *
 * @author RlonRyan
 */
public interface QuikConfigurableInstance {

	String resolve(String input);

}
