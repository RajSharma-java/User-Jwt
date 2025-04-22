package com.user.Controller;

import com.user.Common.ApiResponse;
import com.user.Dto.UserDto;
import com.user.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController
{
    @Autowired
    private UserService userService;

    // create user
    @PostMapping("create")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserDto userDto ){
        UserDto user = userService.createUser(userDto);
        ApiResponse response= new ApiResponse<>(
          true,
          "User created !!!",
          user
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // getById
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(@PathVariable Long id){
        UserDto userDto = userService.getById(id);

        ApiResponse response= new ApiResponse<>(
                true,
                "User fetched !!!",
                userDto
        );
        return ResponseEntity.ok(response);
    }

}
