package org.example.wordaholic_be.converter;

import org.example.wordaholic_be.dto.request.SignUpRequest;
import org.example.wordaholic_be.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private final ModelMapper modelMapper;

    public UserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertToEntity(SignUpRequest signUpRequest) {
        return modelMapper.map(signUpRequest, User.class);
    }
}