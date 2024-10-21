package org.example.wordaholic_be.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.wordaholic_be.service.PaymentService;
import org.example.wordaholic_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PayOS payOS;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private final Map<Integer, Integer> coinPackages;

    public PaymentServiceImpl() {
        // Define coin packages (price -> coins)
        coinPackages = new HashMap<>();
        coinPackages.put(24000, 50); // 24,000₫ -> 50 coins
        coinPackages.put(50000, 110); // 50,000₫ -> 110 coins
        coinPackages.put(99000, 250); // 99,000₫ -> 250 coins
        coinPackages.put(199000, 600); // 199,000₫ -> 600 coins
    }

    @Override
    public ObjectNode createPaymentLink(String productName, String description, String returnUrl, String cancelUrl, int price) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            // Generate order code based on current time
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));

            ItemData item = ItemData.builder()
                    .name(productName)
                    .price(price)
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(description)
                    .amount(price)
                    .item(item)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
        }
        return response;
    }

    @Override
    public ObjectNode getOrderById(long orderId) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
        }
        return response;
    }

    @Override
    public ObjectNode cancelOrder(long orderId) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderId, null);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
        }
        return response;
    }

    @Override
    public ObjectNode handleWebhook(ObjectNode requestBody) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            // Parse the incoming webhook request directly
            String status = requestBody.path("status").asText(); // Adjust this based on the actual JSON structure
            Long userId = requestBody.path("orderCode").asLong(); // Adjust this as needed
            int amountPaidVND = requestBody.path("amount").asInt(); // Adjust this as needed

            // Check if the payment is successful
            if ("success".equalsIgnoreCase(status)) {
                // Get the coins corresponding to the payment amount
                Integer coinsToAdd = coinPackages.get(amountPaidVND);
                if (coinsToAdd != null) {
                    // Update user's coin balance
                    userService.updateUserCoins(userId, coinsToAdd);
                    response.put("error", 0);
                    response.put("message", "Payment successful and coins updated");
                } else {
                    response.put("error", -1);
                    response.put("message", "Invalid payment amount");
                }
            } else {
                response.put("error", -1);
                response.put("message", "Payment failed or not verified");
            }

            response.set("data", requestBody); // Return the original data
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
        }
        return response;
    }

}
