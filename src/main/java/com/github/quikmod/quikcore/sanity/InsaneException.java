/*
 *
 */
package com.github.quikmod.quikcore.sanity;

/**
 *
 * @author RlonRyan
 */
public class InsaneException extends RuntimeException {
    
    public static final String CHECK_FAILED = " : Failed! Reason : ";

    /**
     * Constructs an instance of <code>InsaneException</code> with the specified
     * detail message.
     *
     * @param check the check that failed.
     * @param msg the detail message.
     */
    public InsaneException(String check, String msg) {
        super(check + CHECK_FAILED + msg);
    }
    
    /**
     * Constructs an instance of <code>InsaneException</code> with the specified
     * detail message.
     *
     * @param check the check that failed.
     * @param msg the detail message.
     * @param cause the cause of this exception.
     */
    public InsaneException(String check, String msg, Throwable cause) {
        super(check + CHECK_FAILED + msg, cause);
    }
    
}
