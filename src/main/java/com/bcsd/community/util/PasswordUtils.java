package com.bcsd.community.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static boolean verifyPassword(String inputPassword, String storedHashedPassword, String storedSalt){
        String hashedInputPassword = PasswordUtils.hashPasswordWithSalt(inputPassword, storedSalt);
        assert hashedInputPassword != null;
        return hashedInputPassword.equals(storedHashedPassword);
    }

    public static String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /*
    테스트 코드
    public static void main(String[] args) {
        try {
            String password = "mySecretPassword";
            String salt = generateSalt();
            String hashedPassword = hashPasswordWithSalt(password, salt);

            System.out.println("원래 비밀번호: " + password);
            System.out.println("생성된 솔트: " + salt);
            System.out.println("해시된 비밀번호: " + hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
     */
}