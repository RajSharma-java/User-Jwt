package com.user.Utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils
{
    public static String hashPassword(String planTextPassword){
        // here generate secure hash password using BCrypt
        return BCrypt.hashpw(planTextPassword,BCrypt.gensalt());
    }
}
