package com.example.demo.controller;

import com.example.demo.dto.AnalisResultRespouns;
import com.example.demo.service.AnalisService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AnalisController {
    AnalisService analisService;


    public AnalisController(AnalisService analisService){
        this.analisService = analisService;
    }

    public ResponseEntity <List<AnalisResultRespouns>> getAllAnalysisTicket(@PathVariable Long id){
        List<AnalisResultRespouns>  resultRespouns = analisService.get
    }
}
