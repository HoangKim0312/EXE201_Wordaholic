package org.example.wordaholic_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DictionaryDto {
    private String word;
    private String meaning;
    private char lastLetter;

}
