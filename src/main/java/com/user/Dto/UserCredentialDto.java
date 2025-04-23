package com.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialDto
{


    private String userName;

    private String role;

    private String email;
    private String password;


    private Long userId;

    public UserCredentialDto(Long userId, String role, String userName) {
    }
}
