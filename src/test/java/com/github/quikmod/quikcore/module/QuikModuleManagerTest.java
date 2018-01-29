/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.quikmod.quikcore.module;

import com.github.quikmod.quikcore.injection.QuikInjected;
import com.github.quikmod.quikcore.injection.QuikInjector;
import com.github.quikmod.quikcore.module.event.QuikModuleEvent;
import com.github.quikmod.quikcore.reflection.Quik;
import com.github.quikmod.quikcore.reflection.QuikReflector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.github.quikmod.quikcore.register.QuikClassRegister;

/**
 *
 * @author Ryan
 */
@Quik
@QuikModule(id = "test_module_one", name = "Test Module One")
public class QuikModuleManagerTest {

    public QuikModuleManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class QuikModuleManager.
     */
    @Test
    public void testModules() {
        System.out.println("testModules");
        QuikReflector reflector = new QuikReflector();
        QuikModuleManager instance = new QuikModuleManager();
        instance.findModules(reflector);
        instance.createModules();
        instance.sortModules();
        instance.scanModules(reflector);
        instance.initModules();
        instance.loadModules(reflector);
    }

    @QuikClassRegister("fake_register")
    public static void fakeClassRegister(Class<?> clazz) {
        System.out.println("Fake class register recieved class: " + clazz.getCanonicalName());
    }

    @QuikModule.Listener
    private static void testEventListener(QuikModuleEvent event) {
        System.out.println("testEventListener");
        System.out.println("QuikModuleEvent: " + event.getId());
    }

}
