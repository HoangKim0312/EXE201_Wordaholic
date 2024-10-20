package org.example.wordaholic_be.repository;

import org.example.wordaholic_be.entity.Currency;
import org.example.wordaholic_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepo extends JpaRepository<Currency, Long> {
    Currency findByUser(User user);
}
