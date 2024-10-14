package org.example.wordaholic_be.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Phonetic {
    private String text;
    private String audio;
    // ... other fields as needed based on the API response
}