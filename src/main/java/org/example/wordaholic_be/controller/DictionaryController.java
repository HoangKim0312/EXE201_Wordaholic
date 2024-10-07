package org.example.wordaholic_be.controller;

import org.example.wordaholic_be.dto.DictionaryDto;
import org.example.wordaholic_be.service.impl.DictionaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryServiceImpl dictionaryService;

    @GetMapping("/{word}")
    public DictionaryDto getWordMeaning(@PathVariable String word) {
        return dictionaryService.getWordDetails(word);
    }
}
