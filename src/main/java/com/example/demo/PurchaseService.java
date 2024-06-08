package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PurchaseService {
    private static final String MODULBANK_API_URL = "https://pay.modulbank.ru/pay";

    public void createOrder(Map<String, String> params) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Generate signature
        String signature = calculateSignature(params, "00112233445566778899aabbccddeeff");
        // Send to modulebank
        params.put("signature", signature);
        sendOrderToModulbank(params);
        // Save to database
    }

    private void sendOrderToModulbank(Map<String, String> params) {
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params);

        RestTemplate template = new RestTemplate();
        URI location = template.postForLocation(MODULBANK_API_URL, request);
        System.out.println(location);
    } 

    private String calculateSignature(Map<String, String> params, String key)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String queryString = params.entrySet().stream()
                .filter(entry -> entry.getValue().length() > 0)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> String.format("%s=%s",
                        entry.getKey(),
                        Base64.getEncoder().encodeToString(entry.getValue().getBytes())))
                .collect(Collectors.joining("&"));

        return calculateDigest(queryString, key);
    }

    private String calculateDigest(String queryString, String key)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        HexFormat hf = HexFormat.of();
        md.update(key.getBytes("UTF-8"));
        md.update(queryString.getBytes("UTF-8"));
        String digest = hf.formatHex(md.digest());

        md.reset();
        md.update(key.getBytes("UTF-8"));
        md.update(digest.getBytes("UTF-8"));
        byte[] result = md.digest();
        return hf.formatHex(result);
    }
}
