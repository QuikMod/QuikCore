/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.module;

import com.github.quikmod.quikcore.core.QuikCore;
import com.github.quikmod.quikcore.reflection.QuikReflector;
import com.github.quikmod.quikcore.util.TypeHelper;
import com.github.quikmod.quikcore.util.WrapperCreationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 * @author Ryan
 */
public class QuikModuleManager {

    private Set<Class<?>> moduleClasses = Collections.EMPTY_SET;
    private Map<String, QuikModuleWrapper<?>> modules = Collections.EMPTY_MAP;
    private List<QuikModuleWrapper<?>> sortedModules = Collections.EMPTY_LIST;

    public void findModules(QuikReflector reflector) {
        QuikCore.getCoreLogger().info("Finding @QuikModules...");
        this.moduleClasses = reflector.findQuikClasses()
                .filter(c -> c.isAnnotationPresent(QuikModule.class))
                .peek(c -> QuikCore.getCoreLogger().info("Discovered @QuikModule: {0}", c))
                .collect(Collectors.toSet());
        QuikCore.getCoreLogger().info("Found {0} @QuikModules!", this.moduleClasses.size());
    }

    public void createModules() {
        QuikCore.getCoreLogger().info("Creating @QuikModules...");
        this.modules = this.moduleClasses.stream()
                .map(QuikModuleManager::wrapModule)
                .peek(m -> QuikCore.getCoreLogger().info("Created @QuikModule:\n{0}", m))
                .collect(Collectors.toMap(m -> m.getId(), m -> m));
        QuikCore.getCoreLogger().info("Created {0} @QuikModules!", this.modules.size());
    }
    
    public void sortModules() {
        QuikCore.getCoreLogger().info("Sorting @QuikModules...");
        // An atomic int counter.
        final AtomicInteger counter = new AtomicInteger();
        // This is just a dummy sorter.
        this.sortedModules = this.modules.values().stream()
                .peek(m -> QuikCore.getCoreLogger().info("@QuikModule #{0}: {1}", counter.incrementAndGet(), m.getId()))
                .collect(Collectors.toList());
        QuikCore.getCoreLogger().info("Sorted {0} @QuikModules!", counter.get());
    }

    public void scanModules(QuikReflector reflector) {
        QuikCore.getCoreLogger().info("Scanning @QuikModules...");
        this.sortedModules.forEach(m -> m.scan(reflector));
        QuikCore.getCoreLogger().info("Scanned {0} @QuikModules!", this.sortedModules.size());
    }
    
    public void initModules() {
        QuikCore.getCoreLogger().info("Initializing @QuikModules...");
        this.sortedModules.forEach(m -> m.init());
        QuikCore.getCoreLogger().info("Initialized {0} @QuikModules!", this.sortedModules.size());
    }
    
    public void loadModules(QuikReflector reflector) {
        QuikCore.getCoreLogger().info("Loading @QuikModules...");
        this.sortedModules.forEach(m -> m.load(reflector));
        QuikCore.getCoreLogger().info("Loaded {0} @QuikModules!", this.sortedModules.size());
    }

    private static QuikModuleWrapper<?> wrapModule(Class<?> clazz) {
        try {
            return new QuikModuleWrapper(clazz);
        } catch (WrapperCreationException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Class<?>> getModuleClasses() {
        return moduleClasses;
    }

    public Map<String, QuikModuleWrapper<?>> getModules() {
        return Collections.unmodifiableMap(this.modules);
    }

    public List<QuikModuleWrapper<?>> getSortedModules() {
        return Collections.unmodifiableList(this.sortedModules);
    }

    public boolean hasModule(String moduleId) {
        return this.modules.containsKey(moduleId);
    }

    public Optional<QuikModuleWrapper<?>> getModule(String moduleId) {
        return Optional.ofNullable(this.modules.get(moduleId));
    }
    
    public <T> Optional<QuikModuleWrapper<T>> getModule(String moduleId, Class<T> moduleType) {
        return getModule(moduleId).filter(m -> m.getType().equals(moduleType)).map(m -> (QuikModuleWrapper<T>)m);
    }
    
    public <T> Optional<QuikModuleWrapper<T>> getModule(Class<T> moduleType) {
        return this.sortedModules.stream()
                .filter(m -> TypeHelper.isType(m.getInstance(), moduleType))
                .findAny()
                .map(m -> (QuikModuleWrapper<T>)m);
    }

}
