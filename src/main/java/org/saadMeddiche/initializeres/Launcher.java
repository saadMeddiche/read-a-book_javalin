package org.saadMeddiche.initializeres;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.utils.ClassScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
public class Launcher {

    static {
        log.info("Starting Launcher...");
        List<Class<?>> classes = ClassScanner.findClassesByPackageAndInterface("org.saadMeddiche.initializeres.impl", Initializable.class);

        for (Class<?> clazz : classes) {

            try {
                Initializable initializable = (Initializable) clazz.getDeclaredConstructor().newInstance();
                initializable.initialize();
                log.info("Initialized class: {}.", clazz.getName());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                log.warn("Failed to instantiate class: {}.", clazz.getName(), ex);
            }


        }

        log.info("Launcher finished.");
    }

}
