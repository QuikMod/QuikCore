/*
 * 
 */
package com.github.quikmod.quikcore.core;

/**
 *
 * @author RlonRyan
 */
public enum QuikCoreState {
    /**
     * QuikCore has somehow entered an impossible state, and is unusable.
     */
    INVALID,
    /**
     * An unrecoverable error occurred in QuikCore.
     */
    ERRORED,
    /**
     * QuikCore has yet to be initialized, and is waiting for the setup function
     * to be called. When the initialization function is called on the core, the
     * system transitions into the initializing state.
     */
    WATING,
    /**
     * QuikCore is in the process of initializing, during which all the core
     * services are setup. If no errors are encountered initializing the core
     * services, the system transitions into the checking phase. If an error
     * occurs the system transitions into the error state.
     */
    INITIALIZING,
    /**
     * QuikCore is performing environment sanity checks. If the environment
     * checks out, the system transitions into the scanning state. If the
     * environment fails to check out, the system transitions to the error
     * state.
     */
    CHECKING,
    /**
     * QuickCore is in the process of discovering classes and modules. If no
     * errors are encountered during this process, the system transitions to the
     * delineating state. If an issue occurs scanning classes then the system
     * transitions into the error state.
     */
    SCANNING,
    /**
     * QuikCore is determining the domains of classes and packages. If no errors
     * are encountered during this process, the system transitions to the
     * registering state. If an issue occurs configuring QuikCore, the system
     * transitions into the error state.
     */
    DELINEATING,
    /**
     * QuikCore is registering all Quik classes. If no errors are encountered
     * during this phase, the system transitions into the loading state. If an
     * error occurs performing the registrations, then the system transitions
     * into the error state.
     */
    REGISTERING,
    /**
     * QuikCore is loading all the registered registries with the scanned
     * classes. If no errors are encountered during this phase, the system
     * transitions into the configuring phase. If an issue occurs performing the
     * registrations, then the system transitions into the error state.
     */
    LOADING,
    /**
     * QuikCore is configuring the system based off of the configuration files.
     * If no errors are encountered during this phase, the system transitions
     * into the injecting state. If an error occurs performing the
     * registrations, then the system transitions into the error state.
     */
    CONFIGURING,
    /**
     * QuikCore is performing all static injections. If no errors are
     * encountered while performing injections, then the system transitions into
     * the verifying state. If an issue occurs performing injections, then the
     * system transitions into the error state.
     */
    INJECTING,
    /**
     * QuikCore is running verification checks, as to ensure sanity. If the
     * verification tests pass, then the system transitions into the finished
     * state, and external operations resume. If the verification tests fail,
     * then the system transitions into the error state.
     */
    VERIFYING,
    /**
     * QuikCore has been successfully initialized, and is ready for normal
     * function.
     */
    READY;

    public final boolean canTransition(QuikCoreState toState) {
        switch (toState) {
            case INVALID:
            case ERRORED:
                return this.ordinal() > 1;
            default:
                return this.ordinal() + 1 == toState.ordinal();
        }
    }

}
