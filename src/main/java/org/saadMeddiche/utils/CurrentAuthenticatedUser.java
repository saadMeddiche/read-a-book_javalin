package org.saadMeddiche.utils;

import org.saadMeddiche.entities.User;
import org.saadMeddiche.repositories.UserRepository;
import org.saadMeddiche.repositories.impl.UserSimpleRepository;

import java.util.Optional;

public class CurrentAuthenticatedUser {

    // This should be changed when implementing real authentication
    public static Optional<User> retrieve() {
        UserRepository userRepository = UserSimpleRepository.INSTANCE;
        return userRepository.retrieveById(1L);
    }

}
