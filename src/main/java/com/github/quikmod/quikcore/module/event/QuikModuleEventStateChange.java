/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.module.event;

import com.github.quikmod.quikcore.module.QuikModuleState;

/**
 *
 * @author Ryan
 */
public final class QuikModuleEventStateChange extends QuikModuleEvent {
    
    private final QuikModuleState from;
    private final QuikModuleState to;
    
    public QuikModuleEventStateChange(QuikModuleState from, QuikModuleState to) {
        super("state_change");
        this.from = from;
        this.to = to;
    }

    public QuikModuleState getFrom() {
        return from;
    }

    public QuikModuleState getTo() {
        return to;
    }
    
}
