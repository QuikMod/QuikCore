/*
 */
package com.github.quikmod.quikcore.core;

import com.github.quikmod.quikcore.config.QuikConfig;
import com.github.quikmod.quikcore.log.QuikLogger;
import com.github.quikmod.quikcore.injection.QuikInjectorManager;
import com.github.quikmod.quikcore.reflection.Quik;
import com.github.quikmod.quikcore.reflection.QuikReflector;
import com.github.quikmod.quikcore.sanity.SanityChecker;

/**
 *
 * @author RlonRyan
 */
@Quik
public final class QuikCore {

    private static final QuikLogger CORE_LOGGER = QuikLogger.getLogger("QuikCore");

    private static QuikReflector reflector;

    private static QuikCoreState state = QuikCoreState.WATING;

    private static long phaseStart = 0;

    private static long phaseEnd = 0;

    private QuikCore() {
    }

    public static QuikCoreState getState() {
        return state;
    }

    public static QuikLogger getCoreLogger() {
        return CORE_LOGGER;
    }

    public static QuikLogger getLogger(String source) {
        return QuikLogger.getLogger(source);
    }

    public static synchronized void setup() {
        // Test if QuickCore can actually be initialized.
        if (QuikCore.state != QuikCoreState.WATING) {
            throw new IllegalStateException("The QuikCore setup function was called but QuikCore was in the " + QuikCore.state + " state, not the required " + QuikCoreState.WATING + " state!");
        }

        // Perform setup with error handling.
        try {
            QuikCore.initialize();
            QuikCore.check();
            QuikCore.scan();
            QuikCore.delineate();
            QuikCore.register();
            QuikCore.load();
            QuikCore.configure();
            QuikCore.inject();
            QuikCore.verify();
            QuikCore.ready();
        } catch (Exception e) {
            QuikCore.transition(QuikCoreState.ERRORED);
            throw new RuntimeException("An exception was encountered during QuikCore's " + QuikCore.state + " phase that caused quickcore to transition into the errored state.", e);
        }
    }

    private static synchronized void initialize() throws Exception {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.INITIALIZING);

        // Step I. Initialize the reflector.
        QuikCore.reflector = new QuikReflector();
    }

    private static synchronized void check() throws Exception {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.CHECKING);

        // Step I. Perform Checks
        SanityChecker.performChecks();
    }

    private static synchronized void scan() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.SCANNING);

        // Step I. Scan
        QuikCore.reflector = new QuikReflector();
    }

    private static synchronized void delineate() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.DELINEATING);

        // Step I. Scan
        QuikCore.reflector.performDelineation();
    }

    private static synchronized void register() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.REGISTERING);

        // Step I. Scan
        QuikCore.reflector.performRegistration();
    }

    private static synchronized void load() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.LOADING);

        // Step I. Scan
        QuikCore.reflector.performLoad();
    }

    private static synchronized void configure() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.CONFIGURING);

        // Step I. Load the config.
        QuikConfig.load();

        // Step II. Save the config.
        QuikConfig.save();
    }

    private static synchronized void inject() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.INJECTING);

        // Step I. Perform injections.
        QuikInjectorManager.performInjections();
    }

    private static synchronized void verify() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.VERIFYING);

        // Step I. Run all verification checks.
        // There are currently no verification checks to run.
    }

    private static synchronized void ready() {
        // Step 0. Transition the state.
        QuikCore.transition(QuikCoreState.READY);

        // Step I. Notify
        QuikCore.getCoreLogger().info("QuikCore Ready!");
    }

    private static synchronized void transition(QuikCoreState toState) throws IllegalStateException {
        if (toState == null) {
            throw new NullPointerException();
        }
        if (QuikCore.state.canTransition(toState)) {
            QuikCore.phaseEnd = System.currentTimeMillis();
            QuikCore.getCoreLogger().info("QuikCore {0} phase finished. ({1} ms)", QuikCore.state, QuikCore.phaseEnd - QuikCore.phaseStart);
            QuikCore.state = toState;
            QuikCore.phaseStart = System.currentTimeMillis();
            QuikCore.getCoreLogger().info("QuikCore {0} phase started.", QuikCore.state);
        } else {
            throw new IllegalStateException("An illegal state transition from the " + QuikCore.state + " state to the " + toState + " was attempted.");
        }
    }

}
