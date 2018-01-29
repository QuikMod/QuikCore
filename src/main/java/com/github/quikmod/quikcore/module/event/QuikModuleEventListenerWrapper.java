/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.module.event;

import com.github.quikmod.quikcore.module.QuikModule;
import com.github.quikmod.quikcore.util.TypeHelper;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 *
 * @author Ryan
 */
public class QuikModuleEventListenerWrapper implements QuikModuleEventListener {

    private final QuikModule.Listener annotation;
    private final Class<? extends QuikModuleEvent> eventType;
    private final MethodHandle handle;
    private final Object container;
    private final String id;

    public static QuikModuleEventListenerWrapper wrap(Method m, Object container) {
        try {
            return new QuikModuleEventListenerWrapper(m, container);
        } catch (WrapperCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public QuikModuleEventListenerWrapper(Method m, Object container) throws WrapperCreationException {
        // Step 1.A. Aquire annotation.
        this.annotation = m.getAnnotation(QuikModule.Listener.class);
        // Step 1.B. Throw error if no annotation is present.
        if (this.annotation == null) {
            throw new WrapperCreationException(m, "A @QuikModule.Listener method must be annotated with the @QuikModule.Listener annotation!");
        }

        // Step 2. Save name.
        this.id = m.getName();

        // Step 3. Ensure Static.
        if (Modifier.isStatic(m.getModifiers())) {
            this.container = null;
        } else if (container != null && TypeHelper.isType(container, m.getDeclaringClass())) {
            this.container = container;
        } else {
            throw new WrapperCreationException(m, "A @QuikModule.Listener method must be static, or a member of a module class!");
        }

        // Step 4. Validate method arguments.
        if (m.getParameterCount() != 1) {
            throw new WrapperCreationException(m, "A @QuikModule.Listener method must accept a single argument!");
        } else if (!QuikModuleEvent.class.isAssignableFrom(m.getParameterTypes()[0])) {
            throw new WrapperCreationException(m, "A @QuikModule.Listener method must accept a QuikModuleEvent (or a subclass) as its argument! This one accepts a " + m.getParameterTypes()[0].getName() + " typed argument!");
        }

        // Step 5. Save event type.
        this.eventType = (Class<? extends QuikModuleEvent>) m.getParameterTypes()[0];

        // Step 6. Make accessible.
        try {
            m.setAccessible(true);
        } catch (SecurityException e) {
            throw new WrapperCreationException(m, "Unable to gain access to a @QuikModule.Listener method!", e);
        }

        // Step 7. Fetch method wrapper.
        try {
            this.handle = MethodHandles.lookup().unreflect(m);
        } catch (IllegalAccessException e) {
            throw new WrapperCreationException(m, "An unknown error occurred attempting to create a @QuikModule.Listener wrapper!", e);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Class<? extends QuikModuleEvent> getEventType() {
        return eventType;
    }

    public QuikModule.Listener getAnnotation() {
        return annotation;
    }

    @Override
    public void onEvent(QuikModuleEvent event) {
        if (TypeHelper.isType(event, this.eventType)) {
            try {
                if (container != null) {
                    this.handle.invoke(this.container, event);
                } else {
                    this.handle.invoke(event);
                }
            } catch (Throwable t) {
                throw new RuntimeException("An error occurred while invoking a @QuikModule.Listener! This is not QuikCore's fault!", t);
            }
        }
    }

    @Override
    public final boolean equals(Object obj) {
        return (obj instanceof QuikModuleEventListenerWrapper) && this.equals((QuikModuleEventListenerWrapper) obj);
    }

    public final boolean equals(QuikModuleEventListenerWrapper other) {
        return (other != null) && (this.id.equals(other.id));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<QuikModuleListenerWrapper>{\n");
        sb.append("\tId: \"").append(this.id).append("\",\n");
        sb.append("\tEvent Type: ").append(this.eventType.getName()).append("\n");
        sb.append("}");
        return sb.toString();
    }

}
