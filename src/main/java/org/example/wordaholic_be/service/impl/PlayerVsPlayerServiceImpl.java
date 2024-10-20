package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.Response.DatamuseResponse;
import org.example.wordaholic_be.Response.DictionaryApiResponse;
import org.example.wordaholic_be.Response.Meaning;
import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.service.PlayerVsPlayerService;
import org.example.wordaholic_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class PlayerVsPlayerServiceImpl implements PlayerVsPlayerService {

    private WordDto currentWord; // The last word played
    private String currentPlayer; // Track whose turn it is (player1 or player2)
    private final RestTemplate restTemplate;
    private final UserService userService;

    private Map<String, String> players; // Store player emails

    public PlayerVsPlayerServiceImpl(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        players = new HashMap<>();
    }

    @Override
    public WordDto startGame(String player1Email, String player2Email) {
        User player1 = userService.findUserByEmail(player1Email);
        User player2 = userService.findUserByEmail(player2Email);

        if (player1 == null || player2 == null) {
            return new WordDto(null, "One or both players not found!", null);
        }

        // Store player emails
        players.put("player1", player1Email);
        players.put("player2", player2Email);

        // Randomly choose who starts first
        currentPlayer = new Random().nextBoolean() ? player1Email : player2Email;

        // Start the game with a random word
        currentWord = getRandomWordFromAPI();
        return currentWord != null ? currentWord : new WordDto(null, "Error fetching a word from the dictionary API.", null);
    }

    @Override
    public WordDto playerTurn(String email, String word) {
        if (currentWord == null) {
            return new WordDto(null, "Game has not started. Please call the start API.", null);
        }

        // Check if it's the current player's turn
        if (!email.equals(currentPlayer)) {
            return new WordDto(null, "It's not your turn! Please wait.", null);
        }

        word = word.toLowerCase();
        char lastLetter = currentWord.getWord().charAt(currentWord.getWord().length() - 1);

        if (word.startsWith(String.valueOf(lastLetter))) {
            WordDto playerWordDetails = getWordDetails(word);
            if (playerWordDetails != null) {
                currentWord = playerWordDetails; // Update current word to player's word
                // Switch turn to the other player
                currentPlayer = getOpponent(email);
                return new WordDto(playerWordDetails.getWord(), playerWordDetails.getDefinition(), "The game is still ongoing.");
            } else {
                return new WordDto(null, "Invalid word! Please try again.", null);
            }
        } else {
            return new WordDto(null, null, "Invalid word! Your word must start with '" + lastLetter + "'. You lost!");
        }
    }

    private String getOpponent(String email) {
        // Get the opponent's email from the players map
        return players.get(email.equals(players.get("player1")) ? "player2" : "player1");
    }

    private WordDto getRandomWordFromAPI() {
        // Same method as in your existing implementation
        char randomLetter = (char) ('a' + new Random().nextInt(26));
        String apiUrl = "https://api.datamuse.com/words?sp=" + randomLetter + "*";

        try {
            ResponseEntity<DatamuseResponse[]> response = restTemplate.getForEntity(apiUrl, DatamuseResponse[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                Random random = new Random();
                String randomWord = response.getBody()[random.nextInt(response.getBody().length)].getWord();
                return getWordDetails(randomWord);
            }
        } catch (Exception e) {
            System.out.println("Error while fetching word from API: " + e.getMessage());
        }

        return null;
    }

    private WordDto getWordDetails(String word) {
        // Same method as in your existing implementation
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        try {
            ResponseEntity<DictionaryApiResponse[]> response = restTemplate.getForEntity(apiUrl, DictionaryApiResponse[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                DictionaryApiResponse[] apiResponse = response.getBody();
                String definition = extractMeaning(apiResponse[0]);
                return new WordDto(word, definition, null); // Set definition and return
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
