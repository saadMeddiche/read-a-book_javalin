package org.saadMeddiche.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

public class OrmLiteEntityScanner {
    public static List<Class<?>> findEntities(String packageName) {
        List<Class<?>> entities = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(packageName)
                .scan()) {

            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(DatabaseTable.class)) {
                entities.add(classInfo.loadClass());
            }

        }
        return entities;
    }
}
