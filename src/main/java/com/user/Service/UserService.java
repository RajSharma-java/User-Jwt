package com.user.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.Common.ApiResponse;
import com.user.Dto.UserDto;
import com.user.Model.User;

public interface UserService
{

    // create user
   ApiResponse<UserDto> createUser(UserDto userDto) throws JsonProcessingException;

    // get by id
    ApiResponse<UserDto> getById(Long id );


    User validatePassword(String email, String password);
}
