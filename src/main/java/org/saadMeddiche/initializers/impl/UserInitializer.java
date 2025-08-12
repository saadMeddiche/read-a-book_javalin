package org.saadMeddiche.initializers.impl;

import lombok.extern.slf4j.Slf4j;
import org.saadMeddiche.initializers.Initializable;
import org.saadMeddiche.repositories.UserRepository;
import org.saadMeddiche.repositories.impl.UserSimpleRepository;
import org.saadMeddiche.requests.UserCreateRequest;

@Slf4j
public class UserInitializer implements Initializable {

    private final UserRepository userRepository = UserSimpleRepository.INSTANCE;

    @Override
    public void initialize() {

        UserCreateRequest userCreateRequest = new UserCreateRequest("Bob","Callahan","TheBob","bobCallahan@gmail.com","password123");
        log.info("Initializing User with request: {}", userCreateRequest);

        userRepository.create(userCreateRequest);

    }

}
