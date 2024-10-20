package com.onlinecode.itnhaque.controller;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@RestController
public class HelloController {

    private String jwtKey = "YkKQIPiw3ltlZ6aSVr8NGq8xTkMIaG460dmJiilAnp97YUhA+0t5eDOSzAaEINFeGpEfKdMDXCByEOtSjrdZrw==";

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World!";
    }

    @GetMapping("/jwtkey")
    public String GetJwtKey() {
        // Tạo một mảng byte ngẫu nhiên với độ dài 64 byte (512 bit)
        byte[] randomBytes = new byte[64]; // 512 bit
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        // Mã hóa thành Base64 để tạo chuỗi jwtKey
        String jwtKey = Base64.getEncoder().encodeToString(randomBytes);

        return jwtKey;
    }

    @GetMapping("/secretkey")
    public SecretKey getSecretKey() {
        byte[] keyBytes = com.nimbusds.jose.util.Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
