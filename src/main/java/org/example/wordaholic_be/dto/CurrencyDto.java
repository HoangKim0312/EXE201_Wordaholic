package org.example.wordaholic_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private Long currencyId;
    private int totalCurrency;
    private String currencyName;
}
