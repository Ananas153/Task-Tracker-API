package nam.nam.model;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private int idUser;
    private String description;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(int id, int idUser, String description, Status status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.idUser = idUser;
        this.description = description;
        this.status = status;
        this.createdAt = createAt;
        this.updatedAt = updateAt;
    }

    public Task(int idUser, String description, Status status) {
        this.idUser = idUser;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id + ", idUser=" + idUser +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createAt=" + createdAt +
                ", updateAt=" + updatedAt +
                '}';
    }
}
