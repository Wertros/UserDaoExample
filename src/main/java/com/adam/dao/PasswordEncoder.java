package com.adam.dao;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
