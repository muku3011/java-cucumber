package com.bdd.feature.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

public class CryptoUtils {

    public static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    public static final int TAG_LENGTH_BIT = 128;
    public static final int SALT_LENGTH_BYTE = 16;
    public static final int IV_LENGTH_BYTE = 12;
    private static final int AES_KEY_BIT = 256;

    public static byte[] getRandomNonce(int nonceSize) {
        byte[] nonce = new byte[nonceSize]; // 16 and 12 byte is possible
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    // Hex representation
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    // AES key derived from a password
    // We use salt to protect rainbow attacks, and it is also a random byte, we can use the same com.cucumber.example.CryptoUtils.getRandomNonce to generate it.
    public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        // iterationCount = 65536
        // keyLength = 256
        KeySpec spec = new PBEKeySpec(password, salt, 65536, AES_KEY_BIT);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    // Print hex with block size split
    @SuppressWarnings("unused")
    public static String hexWithBlockSize(byte[] bytes, int blockSize) {
        String hex = hex(bytes);
        // one hex = 2 chars
        blockSize = blockSize * 2;
        // better idea how to print this?
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < hex.length()) {
            result.add(hex.substring(index, Math.min(index + blockSize, hex.length())));
            index += blockSize;
        }
        return result.toString();
    }

    /*AES Helper methods*/

    // 256 bits AES secret key
    @SuppressWarnings("unused")
    public static SecretKey getAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_BIT, SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }
}
