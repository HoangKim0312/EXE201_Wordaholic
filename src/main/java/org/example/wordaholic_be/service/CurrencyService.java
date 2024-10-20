package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.CurrencyDto;
import org.example.wordaholic_be.entity.User;

public interface CurrencyService {
    CurrencyDto getCurrencyByUser(User user);
}
