package com.user.Repository;

import com.user.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User ,Long >
{
     User findByEmail(String email);
}
