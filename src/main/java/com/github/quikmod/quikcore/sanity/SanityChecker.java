package com.github.quikmod.quikcore.sanity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Performs sanity checks on the environment.
 *
 * @author RlonRyan
 */
public final class SanityChecker {

    private static final boolean reflectionCheck = true;

    public static void performChecks() throws InsaneException {
        // Run reflection checks.
        performReflectionCheck();
    }

    /**
     * Ensures that reflection is working.
     */
    public static void performReflectionCheck() throws InsaneException {

        // Catch-All
        try {
            // Step I. Fetch the target class.
            final Class<?> clazz = SanityChecker.class;

            // Step II. Fetch the target field.
            final Field field;

            try {
                field = clazz.getDeclaredField("reflectionCheck");
            } catch (NoSuchFieldException e) {
                throw new InsaneException("Reflection Check", "Unable to find field in class! This is a serious error!", e);
            }

            // Step III. Attempt to make the target field accessable.
            field.setAccessible(true);

            // Step IV. Attempt to make the target field non-final.
            try {
                definalize(field);
            } catch (Exception e) {
                throw new InsaneException("Reflection Check", "Unable to definalize fields!", e);
            }

            // Step V. Attempt to set the field.
            try {
                field.setBoolean(null, true);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new InsaneException("Reflection Check", "Unable to set values!", e);
            }

            // Step VI. Verify check passed.
            if (!SanityChecker.reflectionCheck) {
                throw new InsaneException("Reflection Check", "Reflection does not work!");
            }

        } catch (SecurityException e) {
            throw new InsaneException("Reflection Check", "Reflection appears to have been disabled!", e);
        }

    }

    public static void definalize(Field field) throws Exception {
        // Fetch modifiers field.
        final Field modifiers = Field.class.getDeclaredField("modifiers");

        // Make the modifiers field accessable.
        modifiers.setAccessible(true);

        // Remove the final modifier from the field.
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }

}
