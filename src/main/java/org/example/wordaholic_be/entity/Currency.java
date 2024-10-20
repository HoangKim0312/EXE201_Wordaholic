package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currencyId;

    @Column(name = "total_currency", nullable = false)
    private int totalCurrency;

    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public void addCoins(int coins) {
        this.totalCurrency += coins;
    }

    // Optionally, a method to subtract coins (ensure it doesn't go negative)
    public void subtractCoins(int coins) {
        if (this.totalCurrency - coins < 0) {
            this.totalCurrency = 0; // Ensure totalCurrency does not go below 0
        } else {
            this.totalCurrency -= coins;
        }
    }
}