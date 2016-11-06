/*
 * An attempt at making the configurations easier.
 */
package com.github.quickmod.quiklib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author RlonRyan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QuikConfigurable {

	QuikConfigCategory category();

	String key();

	String comment();
	
	String min() default "0";
	
	String max() default "1";

}
