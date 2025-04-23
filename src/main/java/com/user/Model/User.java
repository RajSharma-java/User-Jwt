package com.user.Model;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-module")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    @Id
    private Long userId;

    private String name;

    private String email;

    private String password;

    private String deviceToken;
}
