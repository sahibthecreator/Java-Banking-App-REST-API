package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.RegisterDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private ModelMapper mapper;

    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDTO toDTO(User post) {
        UserDTO userD = mapper.map(post, UserDTO.class);
        return userD;
    }

    public User toEntity(UserDTO userD) {
        User user = mapper.map(userD, User.class);
        return user;
    }

    public UserDTO registerDTOToUserDTO(RegisterDTO registerDTO) {
        UserDTO userD = mapper.map(registerDTO, UserDTO.class);
        return userD;
    }

    public User registerDTOToUser(RegisterDTO registerDTO) {
        User user = mapper.map(registerDTO, User.class);
        return user;
    }

    public RegisterDTO userToRegisterDTO(User user) {
        RegisterDTO registerDTO = mapper.map(user, RegisterDTO.class);
        return registerDTO;
    }
}
