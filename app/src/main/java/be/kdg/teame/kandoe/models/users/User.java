package be.kdg.teame.kandoe.models.users;

import lombok.Getter;

public class User {
    @Getter
    private int userId;

    @Getter
    private String username;

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @Getter
    private String email;
}
