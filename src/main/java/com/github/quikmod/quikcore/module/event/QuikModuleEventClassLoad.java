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
public final class QuikModuleEventClassLoad extends QuikModuleEvent {
    
    private final Class<?> clazz;

    public QuikModuleEventClassLoad(Class<?> clazz) {
        super("class_load");
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
    
}
