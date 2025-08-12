package org.saadMeddiche.initializeres.impl;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.initializeres.Initializable;

@Slf4j
public class UserInitializer implements Initializable {

    @Override
    public void initialize() {
        log.info("Initializing User");
    }

}
