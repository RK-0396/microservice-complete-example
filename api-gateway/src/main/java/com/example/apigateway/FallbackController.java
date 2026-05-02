package com.example.apigateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/payment")
    public String paymentFallback() {
        return "Payment Service is currently unavailable. Please try again later. (Circuit Breaker Opened)";
    }
}
