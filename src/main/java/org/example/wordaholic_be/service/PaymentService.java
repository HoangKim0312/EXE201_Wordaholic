package org.example.wordaholic_be.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface PaymentService {
    ObjectNode createPaymentLink(String productName, String description, String returnUrl, String cancelUrl, int price);
    ObjectNode getOrderById(long orderId);
    ObjectNode cancelOrder(long orderId);
    ObjectNode handleWebhook(ObjectNode requestBody);
}