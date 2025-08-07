package org.saadMeddiche.requests;

public record ToDoCreateRequest(
        String title,
        String description
) {}