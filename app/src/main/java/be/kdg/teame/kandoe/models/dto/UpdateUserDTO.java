package be.kdg.teame.kandoe.models.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Value;

@Value
public class UpdateUserDTO {
    private final String username;
    @SerializedName("name")
    private final String firstName;
    @SerializedName("surname")
    private final String lastName;
    private final String email;
    private final String verifyPassword;
}
