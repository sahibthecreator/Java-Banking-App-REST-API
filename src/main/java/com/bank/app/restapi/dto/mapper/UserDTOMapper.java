package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

public class UserDTOMapper {


    @Autowired
    private ModelMapper mapper;


    public UserDTO convertToDto(User post) {
        UserDTO userD = mapper.map(post, UserDTO.class);
        return userD;
    }



    public User convertToEntity(UserDTO userD) throws ParseException {
        User u = mapper.map(userD, User.class);
        //set custom vars here

        if (userD.getId() != null) {
            //get user by id
            //set other fields only retrievable from service here
        }
        return u;
    }
}
