package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.dto.CurrencyDto;
import org.example.wordaholic_be.entity.Currency;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.CurrencyRepo;
import org.example.wordaholic_be.service.CurrencyService;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepo currencyRepository;

    public CurrencyServiceImpl(CurrencyRepo currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public CurrencyDto getCurrencyByUser(User user) {
        Currency currency = currencyRepository.findByUser(user);
        if (currency != null) {
            return new CurrencyDto(currency.getCurrencyId(), currency.getTotalCurrency(), currency.getCurrencyName());
        }
        return null;
    }

}
