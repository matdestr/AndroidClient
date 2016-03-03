package be.kdg.teame.kandoe.models.users;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    @SerializedName("name")
    private String firstName;
    @SerializedName("surname")
    private String lastName;
    private String email;
    private String profilePictureUrl;
}
