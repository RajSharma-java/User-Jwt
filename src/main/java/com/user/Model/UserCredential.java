package com.user.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user.Dto.UserCredentialDto;
import com.user.Utils.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-credential")
@Data

public class UserCredential
{
    private Long id ;

    private String userName;

    private String role;


    private String password;

    @Transient
    private Long userId;

    //  method to set role
    public UserCredential(String userName, String plainPassword, String role){
        String hashPassword = PasswordUtils.hashPassword(plainPassword);
        this.userName=userName;
        this.password=hashPassword;
        this.role=role;
    }


    public UserCredentialDto getDto() {
        UserCredentialDto dto = new UserCredentialDto();
        dto.setUserId(this.id);
        dto.setUserName(this.userName); //  match field name
        dto.setPassword(this.password); // optional in token
        dto.setRole(this.role);

        return dto;
    }

}
