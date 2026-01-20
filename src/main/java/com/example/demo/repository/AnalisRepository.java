package com.example.demo.repository;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.entity.AnalysisResult;
import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  AnalisRepository extends JpaRepository <AnalysisResult, Long> {
    //List <AnalisisReSut>
}
