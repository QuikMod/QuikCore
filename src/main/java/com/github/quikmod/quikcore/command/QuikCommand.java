/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author RlonRyan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface QuikCommand {

	/**
	 * The name of the command.
	 *
	 * @return The name of the command.
	 */
	String name();

	/**
	 * Information about what the command does. Required so that no command goes
	 * undocumented.
	 *
	 * @return A description of the function of the command.
	 */
	String info();

	/**
	 * The permission node required to use the command. Defaults to "all".
	 *
	 * @return The permission node required to use the command.
	 */
	String perm() default "all";

}
