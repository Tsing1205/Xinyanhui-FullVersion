package com.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashWithSalt {
    /*生成盐和带盐哈希值的参考代码
    public static void main(String[] args) throws NoSuchAlgorithmException {
        for (char c = 'a'; c <= 'z'; c++) {
            // 输入明文密码
            String password = c + "12345";
            
            // 生成随机盐值
            String salt = generateSalt();
            
            // 计算带盐的哈希值
            String passwordHashWithSalt = hashPassword(password, salt);
            
            // 输出盐值和哈希结果
            System.out.println("原始密码: " + password);
            System.out.println("盐值: " + salt);
            System.out.println("带盐的哈希值: " + passwordHashWithSalt);
            System.out.println("-----------------------------");
        }
    }
    */

    // 生成随机盐值的方法
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 盐值长度为16字节
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes); // 将盐值编码为字符串
    }

    // 计算带盐哈希值的方法
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String passwordWithSalt = password + salt;
        byte[] hashBytes = md.digest(passwordWithSalt.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes); // 将哈希值编码为字符串
    }
}
