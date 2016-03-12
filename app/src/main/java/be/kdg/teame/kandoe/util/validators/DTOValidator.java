package be.kdg.teame.kandoe.util.validators;

import be.kdg.teame.kandoe.models.users.dto.CreateUserDTO;

// TODO : Use library
public class DTOValidator {
    public static boolean isValid(CreateUserDTO createUserDTO) {
        if (createUserDTO.getUsername() == null || createUserDTO.getUsername().isEmpty())
            return false;

        if (createUserDTO.getFirstName() == null || createUserDTO.getFirstName().isEmpty())
            return false;

        if (createUserDTO.getLastName() == null || createUserDTO.getLastName().isEmpty())
            return false;

        if (createUserDTO.getEmail() == null || createUserDTO.getEmail().isEmpty())
            return false;

        if (createUserDTO.getPassword() == null || createUserDTO.getPassword().isEmpty())
            return false;

        if (createUserDTO.getVerifyPassword() == null || createUserDTO.getVerifyPassword().isEmpty())
            return false;

        /*if (!createUserDTO.getPassword().equals(createUserDTO.getVerifyPassword()))
            return false;*/

        return true;
    }
}
