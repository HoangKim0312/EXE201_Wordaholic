package org.example.wordaholic_be.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.wordaholic_be.dto.PaymentRequestDto;
import org.example.wordaholic_be.dto.PaymentResponseDto;
import org.example.wordaholic_be.entity.Currency;
import org.example.wordaholic_be.entity.Payment;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.service.PaymentService;
import org.example.wordaholic_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(path = "/create-payment")
    public ObjectNode createPayment(@RequestBody ObjectNode requestBody) {
        String productName = requestBody.get("productName").asText();
        String description = requestBody.get("description").asText();
        String returnUrl = requestBody.get("returnUrl").asText();
        String cancelUrl = requestBody.get("cancelUrl").asText();
        int price = requestBody.get("price").asInt();

        return paymentService.createPaymentLink(productName, description, returnUrl, cancelUrl, price);
    }

    @GetMapping(path = "/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        return paymentService.getOrderById(orderId);
    }

    @PutMapping(path = "/{orderId}/cancel")
    public ObjectNode cancelOrder(@PathVariable("orderId") long orderId) {
        return paymentService.cancelOrder(orderId);
    }

    @PostMapping(path = "/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody ObjectNode requestBody) {
        return paymentService.handleWebhook(requestBody);
    }
}