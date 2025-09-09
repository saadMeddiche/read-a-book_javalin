package org.saadMeddiche;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.initializers.Launcher;
import org.saadMeddiche.services.DataGenerationService;
import org.saadMeddiche.web.JavalinApp;

import java.sql.SQLException;

@Slf4j
public class Main {

    public static void main(String[] args) throws SQLException {

        Launcher launcher = new Launcher();
        launcher.start();

        JavalinApp javalinApp = new JavalinApp();
        javalinApp.start();

//        DataGenerationService dataGenerationService = new DataGenerationService();
//        dataGenerationService.generateData();

    }

}