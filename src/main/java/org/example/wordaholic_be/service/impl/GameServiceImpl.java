package org.example.wordaholic_be.service.impl;

import org.example.wordaholic_be.Response.DatamuseResponse;
import org.example.wordaholic_be.Response.DictionaryApiResponse;
import org.example.wordaholic_be.Response.Meaning;
import org.example.wordaholic_be.Response.PointsResponseDto;
import org.example.wordaholic_be.dto.UserDto;
import org.example.wordaholic_be.dto.WordDto;
import org.example.wordaholic_be.entity.Currency;
import org.example.wordaholic_be.entity.Points;
import org.example.wordaholic_be.entity.User;
import org.example.wordaholic_be.repository.CurrencyRepo;
import org.example.wordaholic_be.service.GameService;
import org.example.wordaholic_be.service.PointsService;
import org.example.wordaholic_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class GameServiceImpl implements GameService {

    private WordDto currentWord;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final PointsService pointsService;
    private Timer gameTimer;
    private boolean isGameStarted = false;
    private final CurrencyRepo currencyRepository;

    public GameServiceImpl(RestTemplate restTemplate, UserService userService, PointsService pointsService, CurrencyRepo currencyRepository) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.pointsService = pointsService;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public WordDto startGame(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return new WordDto(null, "User not found!", "Game is still ongoing.");
        }

        Points userPoints = pointsService.getPointsByUser(user);
        userPoints.setGamePoints(0); // Reset game points to 0 at the start of the game
        pointsService.saveOrUpdatePoints(userPoints);

        currentWord = getRandomWordFromAPI();
        isGameStarted = true;
        startGameTimer(email);
        return currentWord != null ? currentWord : new WordDto(null, "Error fetching a word from the dictionary API.", "Game is still ongoing.");
    }

    @Override
    public WordDto playTurn(String email, String word) {
        if (!isGameStarted) {
            return new WordDto(null, null, "Game has not started. Please call the start API.");
        }

        resetGameTimer(email);

        word = word.toLowerCase();
        // Check if the word starts with the last letter of the current word
        if (word.startsWith(String.valueOf(currentWord.getWord().charAt(currentWord.getWord().length() - 1)))) {
            WordDto playerWordDetails = getWordDetails(word);
            if (playerWordDetails != null) {
                currentWord = playerWordDetails;

                User user = userService.findUserByEmail(email);
                Points userPoints = pointsService.getPointsByUser(user);

                // Only use gamePoints to accumulate points during the game
                userPoints.setGamePoints(userPoints.getGamePoints() + word.length()); // Add to gamePoints

                // Save only gamePoints
                pointsService.saveOrUpdatePoints(userPoints);

                return new WordDto(playerWordDetails.getWord(), playerWordDetails.getDefinition(), "Game is still ongoing.");
            } else {
                return endGame(email);
            }
        } else {
            return endGame(email);
        }
    }

    @Override
    public WordDto botTurn() {
        if (!isGameStarted) {
            return new WordDto(null, null, "Game has not started. Please call the start API.");
        }

        resetGameTimer("bot");

        char lastLetter = Character.toLowerCase(currentWord.getWord().charAt(currentWord.getWord().length() - 1));
        String botWord = getBotWordByPrefix(lastLetter);
        if (botWord != null) {
            WordDto botWordDetails = getWordDetails(botWord);
            if (botWordDetails != null) {
                currentWord = botWordDetails;
                return botWordDetails;
            }
        }

        return endGame("bot");
    }

    @Override
    public WordDto endGame(String email) {
        User user = userService.findUserByEmail(email);
        Points userPoints = pointsService.getPointsByUser(user);
        Currency userCurrency = user.getCurrency();

        if (!isGameStarted) {
            return new WordDto(null, null, "Game has already ended. You scored " + userPoints.getGamePoints() + " points.");
        }

        int totalPoints = userPoints.getTotalPoints();
        int gamePoints = userPoints.getGamePoints(); // Game points are retained for end-game processing
        currentWord = null;
        isGameStarted = false;
        stopGameTimer();

        String resultMessage;
        if (gamePoints > 10) {
            // Win case: +10 points, +5 coins
            userPoints.addPoints(10); // Add to total points
            userCurrency.setTotalCurrency(userCurrency.getTotalCurrency() + 5); // Add coins
            resultMessage = "You win! Your total points: " + (gamePoints);
        } else {
            // Lose case: -5 points, +2 coins
            userPoints.subtractPoints(5); // Subtract from total points
            userCurrency.setTotalCurrency(userCurrency.getTotalCurrency() + 2); // Add coins
            resultMessage = "You lost! Your total points: " + (gamePoints);
        }

        // Save points and currency updates
        pointsService.saveOrUpdatePoints(userPoints);
        currencyRepository.save(userCurrency);

        // Update the user entity to persist changes
        userService.updateUser(user.getUserId(), new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.isActive(),
                userPoints.getTotalPoints(),     // Total points (updated)
                userCurrency.getTotalCurrency()  // Total currency
        ));

        // Reset gamePoints after the game ends
        userPoints.setGamePoints(0); // Reset game points for next game
        pointsService.saveOrUpdatePoints(userPoints);

        return new WordDto(null, null, resultMessage);
    }

    @Override
    public PointsResponseDto checkPoints(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return null; // Or throw a custom exception
        }

        Points userPoints = pointsService.getPointsByUser(user);
        return new PointsResponseDto(userPoints.getPointsId(), userPoints.getTotalPoints());
    }

    private void startGameTimer(String email) {
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                endGame(email);
            }
        }, 15000);
    }

    private void resetGameTimer(String email) {
        stopGameTimer();
        startGameTimer(email);
    }

    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    private WordDto getRandomWordFromAPI() {
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

    private String getBotWordByPrefix(char lastLetter) {
        String apiUrl = "https://api.datamuse.com/words?sp=" + lastLetter + "*";

        try {
            ResponseEntity<DatamuseResponse[]> response = restTemplate.getForEntity(apiUrl, DatamuseResponse[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                Random random = new Random();
                return response.getBody()[random.nextInt(response.getBody().length)].getWord();
            }
        } catch (Exception e) {
            System.out.println("Error while fetching bot word: " + e.getMessage());
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
                return new WordDto(word, definition, "Game is still ongoing.");
            }
        } catch (Exception e) {
            System.out.println("Error while fetching word details: " + e.getMessage());
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