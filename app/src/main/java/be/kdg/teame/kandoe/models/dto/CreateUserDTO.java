package be.kdg.teame.kandoe.models.dto;

import lombok.Value;

@Value
public class CreateUserDTO {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String verifyPassword;
}
