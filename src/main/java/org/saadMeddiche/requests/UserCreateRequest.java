package org.saadMeddiche.requests;

public record UserCreateRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password
) {}
