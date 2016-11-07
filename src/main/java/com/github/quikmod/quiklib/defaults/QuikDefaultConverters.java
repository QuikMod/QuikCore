/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quiklib.defaults;

import com.github.quikmod.quiklib.conversion.QuikConverter;

/**
 *
 * @author RlonRyan
 */
public class QuikDefaultConverters {
    
    @QuikConverter
    public static Boolean getBool(String value) {
        switch (value.charAt(0)) {
            case 'F':
            case 'f':
            case '0':
                return false;
            case 'T':
            case 't':
            case '1':
                return true;
			default:
				return null;
        }
    }

    @QuikConverter
    public static Integer convertInteger(String value) {
        try {
            return Integer.decode(value);
        } catch (NumberFormatException ne) {
			return null;
        }
    }

    @QuikConverter
    public static Double convertDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ne) {
			return null;
        }
    }

    @QuikConverter
    public static Float convertFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ne) {
			return null;
        }
    }

    @QuikConverter
    public static String convertString(String value) {
        return value;
    }

}
