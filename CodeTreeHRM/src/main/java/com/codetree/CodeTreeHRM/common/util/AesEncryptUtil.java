package com.codetree.CodeTreeHRM.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AesEncryptUtil {

    private final SecretKeySpec keySpec;

    public AesEncryptUtil(@Value("${app.encryption.key}") String key) {
        byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 16);
        this.keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isBlank()) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    public String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isBlank()) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(
                    cipher.doFinal(Base64.getDecoder().decode(cipherText)),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }

    /** 복호화 후 앞 6자리만 표시, 나머지는 마스킹 (예: 123456-*******) */
    public String maskRsdntNo(String cipherText) {
        String plain = decrypt(cipherText);
        if (plain == null || plain.length() < 7) return plain;
        return plain.substring(0, 6) + "-*******";
    }
}
