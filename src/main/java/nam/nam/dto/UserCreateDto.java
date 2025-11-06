package nam.nam.dto;

public record UserCreateDto(
        String name,
        String email,
        String password
) {
}
