package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@SpringBootTest
class SpringbootSecurityTestApplicationTests {

    @Test
    void contextLoads() {
    }
    @Value("${system.user.password.secret}") //注入配置的密钥
    private String secret;
    @Test
    public void testPasswordEncoder(){
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(secret);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String pbk1 = pbkdf2PasswordEncoder.encode("123456");

        System.out.println("pbk1: " + pbk1);
        System.out.println("pbk1 password:" + pbkdf2PasswordEncoder.matches("123456",pbk1));

    }

}
