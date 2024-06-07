package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/create-order")
    public Status createOrder() {
        try {
            purchaseService.createOrder();
            return new Status(true, "Payment created");
        } catch(UnsupportedOperationException e) {
            return new Status(false, "Not implemented");
        }
    }
}
