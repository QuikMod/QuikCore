/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.defaults;

import com.github.quikmod.quikcore.conversion.QuikConverter;
import com.github.quikmod.quikcore.reflection.Quik;

/**
 *
 * @author RlonRyan
 */
@Quik
public class QuikDefaultConverters {
	
	@QuikConverter
	public static Boolean convertBool(String value) {
		return Boolean.parseBoolean(value);
	}
	
	@QuikConverter
	public static Integer convertInteger(String value) {
		return Integer.parseInt(value);
	}

	@QuikConverter
	public static Float convertFloat(String value) {
		return Float.parseFloat(value);
	}

	@QuikConverter
	public static Double convertDouble(String value) {
		return Double.parseDouble(value);
	}

	@QuikConverter
	public static String convertString(String value) {
		return value;
	}

}
