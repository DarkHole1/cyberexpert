package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/create-order")
    public Status createOrder(@RequestParam Map<String, String> allParams) {
        try {
            purchaseService.createOrder(allParams);
            return new Status(true, "Payment created");
        } catch(UnsupportedOperationException e) {
            return new Status(false, "Not implemented");
        } catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return new Status(false, "Internal Error");
        }
    }
}
