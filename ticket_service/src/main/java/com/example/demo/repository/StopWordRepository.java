package com.example.demo.repository;

import com.example.demo.entity.StopWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface StopWordRepository extends JpaRepository<StopWord, Long> {

    Optional<StopWord> findByWord(String word);

    boolean existsByWord(String word);

    @Query("SELECT sw.word FROM StopWord sw")
    Set<String> findAllWords();
}