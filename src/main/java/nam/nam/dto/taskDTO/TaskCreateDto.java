package nam.nam.dto.taskDTO;

import nam.nam.model.Status;

public record TaskCreateDto(
        String description,
        Status status
) {
}
