package com.example.demo.repository;

import com.example.demo.entity.KeywordWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface KeywordWeightRepository extends JpaRepository<KeywordWeight, Long> {

    List<KeywordWeight> findByCategoryName(String categoryName);

    @Query("SELECT kw FROM KeywordWeight kw WHERE LOWER(kw.keyword) LIKE LOWER(CONCAT(' ', :text, ' '))")
    List<KeywordWeight> findByKeywordContainingIgnoreCase(@Param("text") String text);

    @Query("SELECT kw FROM KeywordWeight kw WHERE kw.categoryName = :categoryName AND LOWER(kw.keyword) LIKE LOWER(CONCAT(' ', :text, ' '))")
    List<KeywordWeight> findByCategoryNameAndKeywordContainingIgnoreCase(
            @Param("categoryName") String categoryName,
            @Param("text") String text
    );
}