package be.kdg.teame.kandoe.models.organizations;

import java.util.List;

import be.kdg.teame.kandoe.models.users.User;
import lombok.Data;

@Data
public class Organization {
    private int organizationId;
    private String name;

    private User owner;
    private List<User> members;
}
