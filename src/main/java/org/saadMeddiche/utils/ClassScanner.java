package org.saadMeddiche.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> findClassesByPackageAndAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> entities = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(packageName)
                .scan()) {

            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(annotationClass)) {
                entities.add(classInfo.loadClass());
            }

        }
        return entities;
    }
}
