package org.saadMeddiche.repositories;

import org.saadMeddiche.entities.User;
import org.saadMeddiche.requests.UserCreateRequest;
import org.saadMeddiche.requests.UserUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> retrieveById(Long id);

    List<User> retrieveAll();

    void create(UserCreateRequest userCreateRequest);

    void update(long id, UserUpdateRequest userCreateRequest);

    void delete(long id);

}
