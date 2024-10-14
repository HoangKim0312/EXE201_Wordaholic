package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.Response.DictionaryApiResponse;
import org.example.wordaholic_be.Response.Meaning;
import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.service.GameService;
import org.example.wordaholic_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {

    private WordDto currentWord; // The last word played (player or bot)
    private final RestTemplate restTemplate;
    private final UserService userService;

    public GameServiceImpl(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @Override
    public WordDto startGame(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new WordDto(null, "User not found!");
        }

        // Bot provides the first word
        currentWord = getRandomWordFromAPI();
        return currentWord != null ? currentWord : new WordDto(null, "Error fetching a word from the dictionary API.");
    }

    @Override
    public WordDto playTurn(String email, String word) {
        if (currentWord == null) {
            return new WordDto(null, "Game has not started. Please call the start API.");
        }

        word = word.toLowerCase();
        if (word.startsWith(String.valueOf(currentWord.getWord().charAt(currentWord.getWord().length() - 1)))) {
            WordDto playerWordDetails = getWordDetails(word);
            if (playerWordDetails != null) {
                currentWord = playerWordDetails; // Update current word to player's word
                return playerWordDetails; // Return player's word and its definition
            } else {
                return new WordDto(null, "Invalid word!");
            }
        } else {
            return new WordDto(null, "Invalid word! Your word must start with '" +
                    currentWord.getWord().charAt(currentWord.getWord().length() - 1) + "'.");
        }
    }

    // New method for the bot's turn
    public WordDto botTurn() {
        if (currentWord == null) {
            return new WordDto(null, "Game has not started. Please call the start API.");
        }

        String botWord = getBotWord();
        if (botWord != null) {
            WordDto botWordDetails = getWordDetails(botWord);
            if (botWordDetails != null) {
                currentWord = botWordDetails; // Update current word to bot's word
                return botWordDetails; // Return bot's word and its definition
            }
        }
        return new WordDto(null, "Bot cannot find a valid word.");
    }

    private WordDto getRandomWordFromAPI() {
        String apiUrl = "https://random-word-api.herokuapp.com/word";
        try {
            ResponseEntity<String[]> response = restTemplate.getForEntity(apiUrl, String[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String randomWord = response.getBody()[0];
                return getWordDetails(randomWord);
            }
        } catch (Exception e) {
            // Handle API error
        }
        return null;
    }

    private String getBotWord() {
        char lastLetter = currentWord.getWord().charAt(currentWord.getWord().length() - 1);
        String apiUrl = "https://api.example-dictionary.com/words?prefix=" + lastLetter;

        try {
            ResponseEntity<String[]> response = restTemplate.getForEntity(apiUrl, String[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                Random random = new Random();
                return response.getBody()[random.nextInt(response.getBody().length)];
            }
        } catch (Exception e) {
            // Handle API error
        }
        return null;
    }

    private WordDto getWordDetails(String word) {
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        try {
            ResponseEntity<DictionaryApiResponse[]> response = restTemplate.getForEntity(apiUrl, DictionaryApiResponse[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                DictionaryApiResponse[] apiResponse = response.getBody();
                String definition = extractMeaning(apiResponse[0]);
                return new WordDto(word, definition);
            }
        } catch (Exception e) {
            // Handle API error
        }
        return null;
    }

    private String extractMeaning(DictionaryApiResponse apiResponse) {
        for (Meaning meaning : apiResponse.getMeanings()) {
            if ("noun".equalsIgnoreCase(meaning.getPartOfSpeech())) {
                if (meaning.getDefinitions() != null && !meaning.getDefinitions().isEmpty()) {
                    return meaning.getDefinitions().get(0).getDefinition();
                }
            }
        }
        return "Definition not found.";
    }
}
