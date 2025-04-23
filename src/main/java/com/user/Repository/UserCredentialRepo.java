package com.user.Repository;

import com.user.Model.UserCredential;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserCredentialRepo extends MongoRepository<UserCredential, Long>
{
    Optional<UserCredential> findByUserName(String userName);
}
