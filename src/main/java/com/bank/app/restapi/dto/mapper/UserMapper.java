package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.time.LocalDate;

public class UserMapper {

    @Autowired
    private ModelMapper mapper;

    public UserDTO toDTO(User post) {
        UserDTO userD = mapper.map(post, UserDTO.class);
        return userD;
    }

    public User toEntity(UserDTO userD) throws ParseException {
        User user = mapper.map(userD, User.class);
        // set custom vars here

        if (userD.getId() != null) {
            // get user by id
            // set other fields only retrievable from service here
        }
        return user;
    }

    public boolean isValidDTO(UserDTO userDTO) {
        boolean firstNameIsValid = userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty();
        boolean lastNameIsValid = userDTO.getLastName() != null && !userDTO.getLastName().isEmpty();
        boolean emailIsValid = userDTO.getEmail() != null && !userDTO.getEmail().isEmpty();
        boolean passwordIsValid = userDTO.getPassword() != null && !userDTO.getPassword().isEmpty();
        boolean bsnIsValid = userDTO.getBsn() != null && !userDTO.getBsn().isEmpty();
        boolean dateOfBirthIsValid = userDTO.getDateOfBirth() != null && userDTO.getDateOfBirth().isBefore(LocalDate.now());

        //!!!!!!ONLY FOR TESTING PHASE ONLY!!!!!
        boolean roleIsValid = userDTO.getRole() != null;

        return firstNameIsValid && lastNameIsValid && emailIsValid && passwordIsValid && bsnIsValid && dateOfBirthIsValid && roleIsValid;
    }
}