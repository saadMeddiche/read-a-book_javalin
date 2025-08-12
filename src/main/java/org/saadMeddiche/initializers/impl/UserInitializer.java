package org.saadMeddiche.initializers.impl;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.initializers.Initializable;

@Slf4j
public class UserInitializer implements Initializable {

    @Override
    public void initialize() {
        log.info("Initializing User");
    }

}
