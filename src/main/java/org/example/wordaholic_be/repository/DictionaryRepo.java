package org.example.wordaholic_be.repository;

import org.example.wordaholic_be.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictionaryRepo extends JpaRepository<Dictionary, Long> {
    Optional<Dictionary> findByWord(String word);
}
