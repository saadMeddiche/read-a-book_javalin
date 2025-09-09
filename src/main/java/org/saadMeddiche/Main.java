package org.saadMeddiche;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.initializers.Launcher;
import org.saadMeddiche.services.DataGeneratorService;
import org.saadMeddiche.web.JavalinApp;

import java.sql.SQLException;

@Slf4j
public class Main {

    public static void main(String[] args) throws SQLException {

        Launcher launcher = new Launcher();
        launcher.start();

        JavalinApp javalinApp = new JavalinApp();
        javalinApp.start();

        DataGeneratorService dataGenerationService = new DataGeneratorService();
        dataGenerationService.generateData();

    }

}