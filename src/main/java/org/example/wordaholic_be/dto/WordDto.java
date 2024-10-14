package org.example.wordaholic_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WordDto {
    private String word;
    private String definition;
}
