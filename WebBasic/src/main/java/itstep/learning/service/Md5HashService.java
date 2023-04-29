package itstep.learning.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5HashService implements IHashService {
    @Override
    public String getHexHash(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Md5HashService: " + ex.getMessage());
            return null;
        }

        byte[] digest = md.digest(str.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b: digest) {
            hexString.append(String.format("%02x", b ));
        }
        return hexString.toString();
    }
}
