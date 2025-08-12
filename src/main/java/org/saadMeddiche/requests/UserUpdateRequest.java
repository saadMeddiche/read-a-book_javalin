package org.saadMeddiche.requests;

public record UserUpdateRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String passwordForVerification
) {}
