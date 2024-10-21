package org.example.wordaholic_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private String userEmail;
    private BigDecimal amount;
    private String currency;
    private String description;
}