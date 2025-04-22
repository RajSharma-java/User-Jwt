package com.user.Service.impl;

import com.user.Dto.UserDto;
import com.user.Model.User;
import com.user.Repository.UserRepo;
import com.user.Utils.PasswordUtils;
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

    @Override
    public UserDto createUser(UserDto userDto) {
        // Make sure userDto ID is null so it doesn't overwrite generated one
        userDto.setUserId(null);
        User user = mapper.map(userDto, User.class);
        Optional<User> email = userRepo.findByEmail(user.getEmail());
        if(email.isPresent()){
            throw new RuntimeException("Email is already present");
        }
        user.setUserId(sequenceGeneratorService.generateSequence("user_sequence"));

        // Hash the password
        String password = PasswordUtils.hashPassword(user.getPassword());
        user.setPassword(password);

        // Save to DB
        User savedUser = userRepo.save(user);

        // Return the DTO
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserDto.class);
    }
}
