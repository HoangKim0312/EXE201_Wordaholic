package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.dto.CurrencyDto;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.service.CurrencyService;
import org.example.wordaholic_be.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final UserService userService;

    public CurrencyController(CurrencyService currencyService, UserService userService) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @GetMapping("/check")
    public CurrencyDto checkCurrency(@RequestParam String email) {
        User user = userService.findUserByEmail(email);
        CurrencyDto currencyDto = currencyService.getCurrencyByUser(user);
        if (currencyDto == null) {
            throw new RuntimeException("Currency not found for user!");
        }
        return currencyDto; // Return the user's currency details
    }
}
