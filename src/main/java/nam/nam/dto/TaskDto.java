package nam.nam.dto;

import nam.nam.model.Status;

import java.time.LocalDateTime;

// Use case: When I don't want to expose sensitive data from the database to the user interface.
// Protect data from leaking, keep everything internal to the API.
public class TaskDto {
    private int id;
    private String description;
    private Status status;
    private LocalDateTime createdAt;

    public TaskDto(int id, String description, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }
}
