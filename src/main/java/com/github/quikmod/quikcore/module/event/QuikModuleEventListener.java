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
public interface QuikModuleEventListener {
    
    public String getId();
    
    public Class<? extends QuikModuleEvent> getEventType();
    
    public void onEvent(QuikModuleEvent event);
    
}
