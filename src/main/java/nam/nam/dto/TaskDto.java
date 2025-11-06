package nam.nam.dto;

import nam.nam.model.Status;

import java.time.LocalDateTime;

// Use case: When I don't want to expose sensitive data from the database to the user interface.
// Protect data from leaking, keep everything internal to the API.
public record TaskDto(
        int id,
        String description,
        Status status,
        LocalDateTime createdAt
) {
}