package org.saadMeddiche.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDoResponse {
     public Long id;
     public String title;
     public String description;
     public String userFullName;
 }
