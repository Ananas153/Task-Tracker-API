package nam.nam.dto.taskDTO;

import nam.nam.model.Status;

public record TaskUpdateDto(
        String description,
        Status status
) {
}
