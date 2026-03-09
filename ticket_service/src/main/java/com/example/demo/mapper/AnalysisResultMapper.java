package com.example.demo.mapper;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.entity.AnalysisResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnalysisResultMapper {

    AnalysisResultMapper INSTANCE = Mappers.getMapper(AnalysisResultMapper.class);

    AnalisResultRespouns toDto(AnalysisResult analysisResult);
}
