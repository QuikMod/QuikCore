/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.module;

/**
 *
 * @author Ryan
 */
public enum QuikModuleState {
    
    INVALID,
    CREATING,
    CREATED,
    SCANNING,
    SCANNED,
    INITIALIZING,
    INITIALIZED,
    LOADING,
    LOADED,
    READY;
    
    boolean canTransitionTo(QuikModuleState state) {
        return (this != INVALID) && (state != null) && ((state == INVALID) || (this.ordinal() + 1 == state.ordinal()));
    }
    
}
