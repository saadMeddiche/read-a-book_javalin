package org.saadMeddiche.requests;

public record ToDoUpdateRequest(
        String title,
        String description
) {}