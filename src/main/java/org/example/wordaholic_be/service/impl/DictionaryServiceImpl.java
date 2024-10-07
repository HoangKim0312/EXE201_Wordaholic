package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.Response.DictionaryApiResponse;
import org.example.wordaholic_be.Response.Meaning;
import org.example.wordaholic_be.dto.DictionaryDto;
import org.example.wordaholic_be.entity.Dictionary;
import org.example.wordaholic_be.repository.DictionaryRepo;
import org.example.wordaholic_be.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryRepo dictionaryRepo;

    @Override
    public DictionaryDto getWordDetails(String word) {
        Optional<Dictionary> optionalDictionary = dictionaryRepo.findByWord(word);
        Dictionary dictionary;

        if (optionalDictionary.isPresent()) {
            dictionary = optionalDictionary.get();
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

            DictionaryApiResponse[] apiResponse;
            try {
                apiResponse = restTemplate.getForObject(apiUrl, DictionaryApiResponse[].class);
            } catch (Exception e) {
                throw new RuntimeException("Error fetching data from dictionary API: " + e.getMessage());
            }

            if (apiResponse == null || apiResponse.length == 0) {
                throw new RuntimeException("Meaning not found for the word: " + word);
            }

            String meaning = extractMeaning(apiResponse[0]);
            dictionary = new Dictionary();
            dictionary.setWord(word);
            dictionary.setMeaning(meaning);
            dictionaryRepo.save(dictionary);
        }

        char lastLetter = word.charAt(word.length() - 1);
        return new DictionaryDto(dictionary.getWord(), dictionary.getMeaning(), lastLetter);
    }

    private String extractMeaning(DictionaryApiResponse apiResponse) {
        for (Meaning meaning : apiResponse.getMeanings()) {
            if ("noun".equalsIgnoreCase(meaning.getPartOfSpeech())) {
                if (meaning.getDefinitions() != null && !meaning.getDefinitions().isEmpty()) {
                    return meaning.getDefinitions().get(0).getDefinition();
                }
            }
        }

        throw new RuntimeException("No noun definition found in the response.");
    }

}
