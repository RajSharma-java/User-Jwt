package com.user.Service;

import com.user.Dto.UserDto;

public interface UserService
{

    // create user
    UserDto createUser(UserDto userDto);

    // get by id
    UserDto getById(Long id );
    // getAll
}
