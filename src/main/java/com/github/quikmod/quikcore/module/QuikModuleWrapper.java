/*
 * 
 */
package com.github.quikmod.quikcore.module;

import com.github.quikmod.quikcore.module.event.QuikModuleEventListenerWrapper;
import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.injection.QuikInjectedWrapper;
import com.github.quikmod.quikcore.injection.QuikInjectorWrapper;
import com.github.quikmod.quikcore.module.event.QuikModuleEventClassLoad;
import com.github.quikmod.quikcore.module.event.QuikModuleEvent;
import com.github.quikmod.quikcore.module.event.QuikModuleEventListener;
import com.github.quikmod.quikcore.module.event.QuikModuleEventStateChange;
import com.github.quikmod.quikcore.reflection.QuikReflector;
import com.github.quikmod.quikcore.util.ReflectionStreams;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.lang.reflect.Modifier;

/**
 *
 * @param <T> The module type.
 */
public final class QuikModuleWrapper<T> {

    private final String id;
    private final String name;
    private final T instance;
    private final Class<T> type;
    private final List<String> dependencies;
    private final Set<QuikInjectorWrapper> injectors;
    private final Set<QuikInjectedWrapper> injectables;
    private final Set<QuikModuleEventListener> listeners;

    private Set<Class<?>> classes = Collections.EMPTY_SET;
    
    private QuikModuleState state = QuikModuleState.CREATING;

    public QuikModuleWrapper(Class<T> module) throws WrapperCreationException {        
        // Fetch Command Annotation
        QuikModule q = module.getAnnotation(QuikModule.class);
        if (q == null) {
            throw new WrapperCreationException(module, "A @QuickModule method must be annotated with @QuikModule!");
        }

        // Ensure module is of a public type.
        if (!Modifier.isPublic(module.getModifiers())) {
            throw new WrapperCreationException(module, "A @QuickModule class must be public!");
        }

        // Save type.
        this.type = module;

        // Instantiate.
        try {
            this.instance = module.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new WrapperCreationException(module, "Unable to instantiate quickmodule!", e);
        }

        // Transfer id.
        this.id = q.id().trim().toLowerCase();

        // Transfer name.
        this.name = q.name().trim();

        // Transfer dependencies.
        this.dependencies = Arrays.stream(q.dependsOn())
                .map(e -> e.trim().toLowerCase())
                .collect(Collectors.toList());

        // Allocate internal objects.
        this.classes = new HashSet<>();
        this.injectors = new HashSet<>();
        this.injectables = new HashSet<>();
        this.listeners = new HashSet<>();
        
        // Transition the state.
        this.transitionTo(QuikModuleState.CREATED);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public T getInstance() {
        return instance;
    }

    public Class<T> getType() {
        return type;
    }

    public List<String> getDependencies() {
        return Collections.unmodifiableList(this.dependencies);
    }

    public Set<Class<?>> getClasses() {
        return Collections.unmodifiableSet(this.classes);
    }

    public Set<QuikInjectorWrapper> getInjectors() {
        return Collections.unmodifiableSet(this.injectors);
    }

    public Set<QuikInjectedWrapper> getInjectables() {
        return Collections.unmodifiableSet(this.injectables);
    }

    public void scan(QuikReflector reflector) {
        // Transition the state.
        this.transitionTo(QuikModuleState.SCANNING);
        this.classes = reflector.findQuikClasses()
                .filter(c -> c.getName().startsWith(this.type.getPackage().getName()))
                .collect(Collectors.toSet());
        // Transition the state.
        this.transitionTo(QuikModuleState.SCANNED);
    }

    public void init() {
        // Transition the state.
        this.transitionTo(QuikModuleState.INITIALIZING);
        // Load listeners.
        this.classes.stream()
                .flatMap(ReflectionStreams::streamMethods)
                .filter(m -> m.isAnnotationPresent(QuikModule.Listener.class))
                .map(m -> QuikModuleEventListenerWrapper.wrap(m, this.instance))
                .forEach(this.listeners::add);
        // Transition the state.
        this.transitionTo(QuikModuleState.INITIALIZED);
    }

    public void load(QuikReflector reflector) {
        // Transition the state.
        this.transitionTo(QuikModuleState.LOADING);
        // Load each class, by firing an event with the class.
        reflector.findQuikClasses()
                .map(QuikModuleEventClassLoad::new)
                .forEach(this::dispatchEvent);
        // Transition the state.
        this.transitionTo(QuikModuleState.LOADED);
    }
    
    public void ready() {
        // Transition the state.
        this.transitionTo(QuikModuleState.READY);
    }

    public void addListener(QuikModuleEventListener listener) {
        this.listeners.add(listener);
    }

    public void dispatchEvent(QuikModuleEvent event) {
        // Log event dispatch.
        QuikCore.getCoreLogger().info("Starting dispatch of QuikModuleEvent: \"{0}\" across module: \"{1}\"!", event.getId(), this.getId());
        // Fire off the event.
        this.listeners.forEach(l -> l.onEvent(event));
        // Log event finish.
        QuikCore.getCoreLogger().info("Finished dispatch of QuikModuleEvent: \"{0}\" across module: \"{1}\"!", event.getId(), this.getId());
    }
    
    private void transitionTo(QuikModuleState state) {
        if (this.state.canTransitionTo(state)) {
            // Save the old state.
            final QuikModuleState oldState = this.state;
            // Update the state.
            this.state = state;
            // Log the state transition.
            QuikCore.getCoreLogger().info("@QuikModule transitioned from state {0} to state {1}!", oldState, state);
            // Dispatch a state change event.
            this.dispatchEvent(new QuikModuleEventStateChange(oldState, state));
        } else {
            throw new IllegalStateException("@QuikModule " + this.id + " attempted to perform illegal state transition from state " + this.state + " to state " + state);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof QuikModuleWrapper) && equals((QuikModuleWrapper) obj);
    }

    public boolean equals(QuikModuleWrapper other) {
        return (other != null) && Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        // Create a string builder.
        StringBuilder sb = new StringBuilder();
        // Add stuff to string builder.
        sb.append("<QuikModuleWrapper> {\n");
        sb.append("\tid: \"").append(this.id).append("\",\n");
        sb.append("\tname: \"").append(this.name).append("\",\n");
        sb.append("\ttype: ").append(this.type.getCanonicalName()).append(",\n");
        sb.append("\tdependencies: ").append(this.dependencies).append("\n");
        sb.append("}");
        // Return the built string.
        return sb.toString();
    }

}
