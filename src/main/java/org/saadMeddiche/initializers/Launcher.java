package org.saadMeddiche.initializers;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.configurations.PackageConfiguration;
import org.saadMeddiche.utils.ClassScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Launcher {

    public void start() {

        log.info("Starting Launcher...");

        List<Class<?>> classes = ClassScanner.findClassesByPackageAndInterface(PackageConfiguration.INSTANCE.INITIALIZER_PACKAGE, Initializable.class);

        Map<Integer, Initializable> initializableMap = createInstancesMap(classes);
        log.info("{} Classes loaded", classes.size());

        initializeProcesses(initializableMap);

        log.info("Launcher finished.");
    }

    /**
     * @return Map<Integer, Initializable>
     * <p> Integer is the execution order for that Initializable class
     */
    private Map<Integer, Initializable> createInstancesMap(List<Class<?>> classes) {

        Map<Integer, Initializable> initializableMap = new HashMap<>();

        for (Class<?> clazz : classes) {

            try {
                Initializable initializable = (Initializable) clazz.getDeclaredConstructor().newInstance();
                initializableMap.put(initializable.order(), initializable);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException ex) {
                log.warn("Failed to instantiate class: {}.", clazz.getName(), ex);
            }

        }

        return initializableMap;
    }

    private void initializeProcesses(Map<Integer, Initializable> initializableMap) {
        int size = initializableMap.size();
        log.info("Start initialization process ...");
        for (int i = 0; i < size; i++) {
            log.info("Process #{} started.", i);

            Initializable initializable = initializableMap.get(i);

            if(initializable == null) {
                log.info("Process #{} not found", i);
                continue;
            }

            if (initializable.isInitialized()) {
                log.info("Process[{}] #{} is already initialized, skipping.", initializable.getClass().getName(), i);
                continue;
            }

            log.info("Initializing Process[{}] #{}", initializable.getClass().getName(), i);
            initializable.initialize();

        }
    }

}
