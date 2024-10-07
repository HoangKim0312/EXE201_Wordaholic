package org.example.wordaholic_be.service;

import org.example.wordaholic_be.dto.DictionaryDto;

public interface DictionaryService {
    DictionaryDto getWordDetails(String word);
}
