/*
 * An attempt at making the configurations easier.
 */
package com.github.quikmod.quikcore.config;

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

	String category();

	String key();

	String comment();
	
	double min() default 0;
	
	double max() default 1;

}
