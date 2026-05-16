package com.example.demo.mapper;

import com.example.demo.dto.KeywordWeightDto;
import com.example.demo.dto.KeywordWeightRequest;
import com.example.demo.entity.KeywordWeight;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KeywordWeightMapper {

    public KeywordWeightDto toDto(KeywordWeight entity) {
        if (entity == null) {
            return null;
        }

        return new KeywordWeightDto(
                entity.getId(),
                entity.getCategoryName(),
                entity.getKeyword(),
                entity.getWeight(),
                entity.getCreatedAt()
        );
    }

    public KeywordWeight toEntity(KeywordWeightRequest request) {
        if (request == null) {
            return null;
        }

        KeywordWeight entity = new KeywordWeight();
        entity.setCategoryName(request.getCategoryName());
        entity.setKeyword(request.getKeyword());
        entity.setWeight(request.getWeight());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public void updateEntityFromRequest(KeywordWeight entity, KeywordWeightRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setCategoryName(request.getCategoryName());
        entity.setKeyword(request.getKeyword());
        entity.setWeight(request.getWeight());
    }
}