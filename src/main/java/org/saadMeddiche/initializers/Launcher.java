package org.saadMeddiche.initializers;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.configurations.PackageConfiguration;
import org.saadMeddiche.utils.ClassScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
public class Launcher {

    public void start() {

        log.info("Starting Launcher...");

        List<Class<?>> classes = ClassScanner.findClassesByPackageAndInterface(PackageConfiguration.INSTANCE.INITIALIZER_PACKAGE, Initializable.class);

        for (Class<?> clazz : classes) {

            try {

                Initializable initializable = (Initializable) clazz.getDeclaredConstructor().newInstance();
                log.info("Initializing class: {}.", clazz.getName());

                if(initializable.isInitialized()) {
                    log.info("Class {} is already initialized, skipping.", clazz.getName());
                    continue;
                }

                initializable.initialize();

            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                log.warn("Failed to instantiate class: {}.", clazz.getName(), ex);
            }

        }

        log.info("Launcher finished.");
    }

}
