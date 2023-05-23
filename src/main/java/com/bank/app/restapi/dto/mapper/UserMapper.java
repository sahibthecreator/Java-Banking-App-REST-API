package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.User;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class UserMapper {

    private ModelMapper mapper;

    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDTO toDTO(User post) {
        UserDTO userD = mapper.map(post, UserDTO.class);
        return userD;
    }

    public User toEntity(UserDTO userD) throws IllegalArgumentException {
        this.isValidDTO(userD);
        User user = mapper.map(userD, User.class);
        return user;
    }

    public void isValidDTO(UserDTO userDTO) throws IllegalArgumentException {
        boolean firstNameIsValid = userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty();
        boolean lastNameIsValid = userDTO.getLastName() != null && !userDTO.getLastName().isEmpty();
        boolean emailIsValid = userDTO.getEmail() != null && !userDTO.getEmail().isEmpty();
        boolean passwordIsValid = userDTO.getPassword() != null && !userDTO.getPassword().isEmpty();
        boolean bsnIsValid = userDTO.getBsn() != null && !userDTO.getBsn().isEmpty();
        boolean dateOfBirthIsValid = userDTO.getDateOfBirth() != null
                && userDTO.getDateOfBirth().isBefore(LocalDate.now());

        // !!!!!!ONLY FOR TESTING PHASE ONLY!!!!!
        // Ye, This looks horrible XD - Yvan
        boolean roleIsValid = userDTO.getRole() != null;

        if (!(firstNameIsValid && lastNameIsValid && emailIsValid && passwordIsValid && bsnIsValid
                && dateOfBirthIsValid && roleIsValid)) {
            throw new IllegalArgumentException("Not all required fields were provided for creation User");
        }
    }
}
