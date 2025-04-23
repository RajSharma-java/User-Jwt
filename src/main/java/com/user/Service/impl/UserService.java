package com.user.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.Common.ApiResponse;
import com.user.Dto.UserCredentialDto;
import com.user.Dto.UserDto;
import com.user.Model.User;
import com.user.Model.UserCredential;
import com.user.Repository.UserRepo;
import com.user.Utils.JwtUtils;
import com.user.Utils.PasswordUtils;
import com.user.Utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeMBeanException;
import java.util.Optional;

@Service
public class UserService implements com.user.Service.UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private UserCredentialService userCredentialService;

    @Override
    public ApiResponse<UserDto> createUser(UserDto userDto) throws JsonProcessingException {
        userDto.setUserId(null);

        // Map UserDto to User entity
        User user = mapper.map(userDto, User.class);

        // Check if email already exists
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email is already present");
        }

        // Generate unique user ID
        user.setUserId(sequenceGeneratorService.generateSequence("user_sequence"));

        // Hash the password
        if(userDto.getPassword()!=null && !userDto.getPassword().isEmpty()){
            String hashedPassword = PasswordUtils.hashPassword(userDto.getPassword());
            user.setPassword(hashedPassword);
        }


        // Generate unique username
        String username = Utils.generateUsername(userDto.getName());
        while (userCredentialService.findByUsername(username) != null) {
            username = Utils.generateUsername(userDto.getName());
        }

        // Create credentials
        UserCredential userCredential = userCredentialService.create(username, userDto.getPassword(), "user");
        userCredential.setId(user.getUserId());

        // Generate JWT token
        String deviceToken = JwtUtils.generateToken(userCredential.getDto());
        user.setDeviceToken(deviceToken);

        // Save user and return response
        User savedUser = userRepo.save(user);
        UserDto responseDto = mapper.map(savedUser, UserDto.class);

        return new ApiResponse<>(true, "User created successfully", responseDto);
    }

    @Override
    public ApiResponse<UserDto> getById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto userDto = mapper.map(user, UserDto.class);

        return new ApiResponse<>(true, "User fetched successfully", userDto);
    }

    // validate Password
    @Override
    public User validatePassword(String email, String plainPassword) {
        User user = userRepo.findByEmail(email);
        if (user != null && PasswordUtils.checkPassword(plainPassword, user.getPassword())) {
            return user;
        }
        return null;
    }





}
