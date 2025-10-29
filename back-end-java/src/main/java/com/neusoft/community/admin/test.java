package com.neusoft.community.admin;

import org.mindrot.jbcrypt.BCrypt;

public class test {
    public static void main(String[] args) {
        String hash = "$2a$10$AiPugTr1Ovbih21vA5ywMO8k3IkEv5.5NE8ejHHEMVklcYtd7T7VS";
        System.out.println(BCrypt.checkpw("user123", hash));
    }

}
