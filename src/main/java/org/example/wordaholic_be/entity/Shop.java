    package org.example.wordaholic_be.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.List;

    @Entity
    @Table(name = "shop")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Shop {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "item_name", nullable = false)
        private String itemName;

        @Column(name = "description")
        private String description;

        @Column(name = "price" ,nullable = false)
        private Double price;

        @Column(name = "quantity" ,nullable = false)
        private Double quantity;

        @Column(name = "available" ,nullable = false)
        private boolean available;

        @ManyToOne
        @JoinColumn(name = "currency_id", nullable = false)
        private Currency currency;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Payment> payments;
    }
