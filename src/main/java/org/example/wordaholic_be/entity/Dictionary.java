package org.example.wordaholic_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dictionaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;

    @Column(unique = true, nullable = false)
    private String word;

    @Column(columnDefinition = "TEXT") // Use "TEXT" for potentially long definitions
    private String meaning;

}