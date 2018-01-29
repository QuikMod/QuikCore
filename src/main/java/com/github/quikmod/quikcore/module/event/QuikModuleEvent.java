/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.module.event;

/**
 *
 * @author Ryan
 */
public class QuikModuleEvent {
    
    private final String id;

    public QuikModuleEvent(String id) {
        this.id = "quik_module_event_" + id;
    }

    public String getId() {
        return id;
    }
    
}
