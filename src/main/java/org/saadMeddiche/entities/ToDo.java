package org.saadMeddiche.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder @NoArgsConstructor @AllArgsConstructor
public class ToDo {
    public Long id;
    public String title;
    public String description;
}
