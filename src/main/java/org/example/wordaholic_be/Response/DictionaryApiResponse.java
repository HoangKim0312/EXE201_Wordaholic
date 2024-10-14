package org.example.wordaholic_be.Response;

import lombok.Data;

import java.util.List;

@Data
public class DictionaryApiResponse {
    private String word;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;
}
