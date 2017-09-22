/*
 */
package com.github.quikmod.quikcore.reflection;

import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author Ryan
 */
public final class QuikReflector {
    
    private final Reflections reflections;
    
    public QuikReflector() {
        this.reflections = new Reflections(
                new ConfigurationBuilder().setScanners(
                        new SubTypesScanner(),
                        new TypeAnnotationsScanner()
                ).forPackages("com", "net", "org")
        );
    }
    
    public Stream<Class<?>> findQuikClasses() {
        return this.reflections.getTypesAnnotatedWith(Quik.class).stream();
    }
    
    public Stream<Package> findQuikPackages() {
        return this.reflections.getTypesAnnotatedWith(QuikDomain.class).stream()
                .filter(c -> c.getSimpleName().equals("package-info"))
                .map(c -> c.getPackage());
    }
    
    public void performDelineation() {
        this.findQuikPackages().forEach(QuikDomains::registerDomain);
        this.findQuikClasses().forEach(QuikDomains::registerDomain);
    }
    
    public void performRegistration() {
        this.findQuikClasses().forEach(QuikRegisterRegistry::registerRegisters);
    }
    
    public void performLoad() {
        this.findQuikClasses().forEach(QuikRegisterRegistry::performRegister);
    }
    
}
