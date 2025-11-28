package nam.nam.mapper;

import nam.nam.dto.taskDTO.TaskGetByIdDto;
import nam.nam.model.Task;

// Could make the toDto method inside the Dto class but it means the Dto object would know directly about the
// Task object in the method's parameter -> tightly coupled -> Separation Concern
public class TaskMapper {
    public static TaskGetByIdDto toDto(Task task) {
        return new TaskGetByIdDto(task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt());
    }
}
