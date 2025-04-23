package com.user.Service.impl;

import com.user.Model.UserCredential;
import com.user.Repository.UserCredentialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialService
{
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private UserCredentialRepo userCredentialRepo;

    // create Role

    public UserCredential create(String username, String password, String role){
        long id = sequenceGeneratorService.generateSequence("user-credential-service");

        UserCredential userCredential= new UserCredential(
          username,password,role
        );
        userCredential.setId(id);
        return userCredentialRepo.save(userCredential);
    }

    public UserCredential findByUsername(String username){
        return userCredentialRepo.findByUserName(username).orElse(null);
    }
}
