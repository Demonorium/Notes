package com.demonorium.webinterface.forms;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterForm {
//    private static MessageDigest digest;
//    private static Charset reverse = StandardCharsets.UTF_8;
//    static {
//        try {
//            digest = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
//        byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
//        this.password = new String(bytes, reverse);
    }
    public void resetPassword() {
        this.password = "";
    }
}
