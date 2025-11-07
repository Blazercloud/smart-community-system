package com.neusoft.community;

import org.mindrot.jbcrypt.BCrypt;

public class passwordsalt {

    public static void main(String[] args) {

    String rawPassword = "123456";
    String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    System.out.println(hashed);


    }
}
