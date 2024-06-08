package com.example.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    public void createOrder(Map<String, String> params) throws NoSuchAlgorithmException {
        // Generate signature
        String signature = calculateSignature(params, "00112233445566778899aabbccddeeff");
        // Send to modulebank
        // Save to database
    }

    private String calculateSignature(Map<String, String> params, String key) throws NoSuchAlgorithmException {
        String queryString = params.entrySet().stream()
                .filter(entry -> entry.getValue().length() > 0)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> String.format("%s=%s",
                        entry.getKey(),
                        Base64.getEncoder().encodeToString(entry.getValue().getBytes())))
                .collect(Collectors.joining("&"));

        return calculateDigest(queryString, key);
    }

    private String calculateDigest(String queryString, String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(key.getBytes());
        md.update(queryString.getBytes());
        byte[] digest = md.digest();
        md.reset();
        md.update(key.getBytes());
        md.update(digest);
        byte[] result = md.digest();
        return HexFormat.of().formatHex(result);
    }
}
