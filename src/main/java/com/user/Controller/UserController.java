package com.user.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.user.Common.ApiResponse;
import com.user.Dto.UserCredentialDto;
import com.user.Dto.UserDto;
import com.user.Interceptor.PublicEndpoint;
import com.user.Model.User;
import com.user.Model.UserCredential;
import com.user.Service.UserService;
import com.user.Utils.JwtUtils;
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

    @Autowired
    private JwtUtils jwtUtils;
    // create user
    @PublicEndpoint
    @PostMapping("signUp")
    public ResponseEntity<ApiResponse<UserDto>> singUp(@RequestBody UserDto userDto ) throws JsonProcessingException {
        UserDto user = userService.createUser(userDto).getData();
        ApiResponse response= new ApiResponse<>(
          true,
          "User created !!!",
          user
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // getById
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(@PathVariable Long id, @RequestAttribute("credential") UserCredentialDto credentialDto) {
        System.out.println("Received Credential: " + credentialDto);
        ApiResponse<UserDto> response = userService.getById(id);
        return ResponseEntity.ok(response);
    }


    // login
    @PublicEndpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserCredentialDto userCredentialDto) throws JsonProcessingException {
        try {
            String email = userCredentialDto.getEmail();
            String password = userCredentialDto.getPassword();

            // Validate email and password
            User user = userService.validatePassword(email, password);
            if (user != null) {
                // Create token using UserCredentialDto
                UserCredentialDto credentialDto = new UserCredentialDto();
                credentialDto.setEmail(email);
                credentialDto.setUserName(user.getName()); // Or username if you store it
                credentialDto.setRole("user"); // If you track role
                String token = JwtUtils.generateToken(credentialDto);

                ApiResponse response = new ApiResponse(
                        true,
                        "Login successful",
                        token
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse errorResponse = new ApiResponse(
                        false,
                        "Invalid email or password",
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log error
            ApiResponse errorResponse = new ApiResponse(
                    false,
                    "Something went wrong",
                    null
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
