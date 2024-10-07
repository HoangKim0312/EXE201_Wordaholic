package org.example.wordaholic_be.Response;

import lombok.Data;

import java.util.List;

@Data
public class Meaning {
    private String partOfSpeech;
    private List<Definition> definitions; // List of definitions
}
