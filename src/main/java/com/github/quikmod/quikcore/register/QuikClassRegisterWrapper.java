/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.register;

import com.github.quikmod.quikcore.util.WrapperCreationException;
import com.google.common.base.Preconditions;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 *
 * @author Ryan
 */
public final class QuikClassRegisterWrapper {
    
    private final String id;
    private MethodHandle handle;
    
    public QuikClassRegisterWrapper(Object container, Method register) throws WrapperCreationException {
		// Ensure that function is injector.
		if (!register.isAnnotationPresent(QuikClassRegister.class)) {
			throw new WrapperCreationException(register, "A @QuikRegister must be annotated with @QuikRegister!");
		}

		// Ensure that the function accepts proper number of arguments.
		if (register.getParameterCount() != 1) {
			throw new WrapperCreationException(register, "A @QuikRegister must accept a single argument! This one accepts " + register.getParameterCount() + "!");
		}

		// Ensure that arguments are of proper type.
		if (!Class.class.isAssignableFrom(register.getParameters()[0].getType())) {
			throw new WrapperCreationException(register, register.getParameters()[0], "A @QuikRegister must accept an argument of type 'Class<?>'! This one accepts an argument of type " + register.getParameters()[0].getType().getTypeName() + "!");
		}

		// Ensure method is accessable from this context.
		if (Modifier.isStatic(register.getModifiers())) {
			container = null;
		} else if (container == null) {
			throw new WrapperCreationException(register, "A @QuikRegister must be a static method if no instance is given!");
		} else if (!register.getDeclaringClass().isAssignableFrom(container.getClass())) {
			throw new WrapperCreationException(register, "Invalid @QuikRegister container '" + container.getClass().getName() + "'!");
		}
        
        
        // Force-set the method's accessibility, as to get around private methods.
        try {
            register.setAccessible(true);
        } catch (SecurityException e) {
            throw new WrapperCreationException(register, "Unable to gain access to a @QuikRegister's method!", e);
        }

		// Set the name.
		this.id = "register$root." + register.getAnnotation(QuikClassRegister.class).value();

		// Fetch the handle.
		try {
			if (container == null) {
				this.handle = MethodHandles.lookup().unreflect(register);
			} else {
				this.handle = MethodHandles.lookup().unreflect(register).bindTo(container);
			}
		} catch (IllegalAccessException e) {
			throw new WrapperCreationException(register, "An unknown error occured attempting to convert a @QuikRegister's method to a method handle.", e);
		}
	}
    
    public void register(Class<?> clazz) {
        // Ensure non-null
        Preconditions.checkNotNull(clazz, "Attempted to register null class to @QuikRegister {0}!", this.id);
        try {
            this.handle.invoke(clazz);
        } catch (Throwable t) {
            throw new RuntimeException("Error registering class: '" + clazz.getCanonicalName() + "' to register: " + this.id, t);
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof QuikClassRegisterWrapper) && equals((QuikClassRegisterWrapper) obj);
    }

    public boolean equals(QuikClassRegisterWrapper other) {
        return (other != null) && Objects.equals(this.id, other.id);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        // Create a string builder.
        StringBuilder sb = new StringBuilder();
        // Add stuff to string builder.
        sb.append("<QuikClassRegisterWrapper> {\n");
        sb.append("\tid: \"").append(this.id).append("\",\n");
        sb.append("\thandle: ").append(this.handle).append("\n");
        sb.append("}\n");
        // Return the built string.
        return sb.toString();
    }
    
}
