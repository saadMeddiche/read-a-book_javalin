package org.saadMeddiche.requests;

public record BookCreateRequest(
        String title,
        String author,
        String summary
) {}
