package org.example.wordaholic_be.Response;

import lombok.Data;

import java.util.List;

@Data
public class DictionaryApiResponse {
    private List<Meaning> meanings; // List of meanings
}
